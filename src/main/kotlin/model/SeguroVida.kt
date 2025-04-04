package model
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeguroVida : Seguro {

    private val fechaNac: LocalDate
    private val nivelRiesgo: Riesgo
    private val indemnizacion: Double

    private constructor(
        numPoliza: Int,
        dniTitular: String,
        importe: Double,
        fechaNac: LocalDate,
        nivelRiesgo: Riesgo,
        indemnizacion: Double
    ) : super(numPoliza, dniTitular, importe) {
        this.fechaNac = fechaNac
        this.nivelRiesgo = nivelRiesgo
        this.indemnizacion = indemnizacion
    }

    constructor(
        dniTitular: String,
        importe: Double,
        fechaNac: LocalDate,
        nivelRiesgo: Riesgo,
        indemnizacion: Double
    ) : this(
        numPoliza = ++numPolizasVida,
        dniTitular = dniTitular,
        importe = importe,
        fechaNac = fechaNac,
        nivelRiesgo = nivelRiesgo,
        indemnizacion = indemnizacion
    )

    companion object {
        internal var numPolizasVida: Int = 800000

        fun crearSeguro(datos: List<String>): SeguroVida {
                val numPoliza = datos[0].toInt()
                val dniTitular = datos[1]
                val importe = datos[2].toDouble()
                val formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val fechaNac = LocalDate.parse(datos[3], formateador)
                val nivelRiesgo = Riesgo.valueOf(datos[4].uppercase())
                val indemnizacion = datos[5].toDouble()

                return SeguroVida(numPoliza, dniTitular, importe, fechaNac, nivelRiesgo, indemnizacion)

        }
    }

    /** Retorna el importe del año siguiente basándose en el interés que se pasa por parámetro,
     * sumándole un interés residual del 0.05% por cada año cumplido y el interés de su nivel de riesgo */
    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val edad = calcularEdad()

        val incremento = (edad.year * 0.0005) + nivelRiesgo.interesAplicado
        return importe * (1 + interes + incremento)
    }

    override fun serializar(separador: String): String {
        val formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaStr = fechaNac.format(formateador)

        return super.serializar(separador) + separador +
                "$fechaStr$separador$nivelRiesgo$separador$indemnizacion$separador${tipoSeguro()}"
    }

    private fun calcularEdad(): LocalDate {
        val anio = LocalDate.now().year - fechaNac.year
        val mes = LocalDate.now().monthValue - fechaNac.monthValue
        val dia = LocalDate.now().dayOfMonth - fechaNac.dayOfMonth
        val formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return LocalDate.parse("$dia/$mes/$anio", formateador)
    }

    override fun toString(): String {
        return "Seguro Vida(${super.toString()}, fechaNac=$fechaNac, nivelRiesgo=$nivelRiesgo, indemnizacion=$indemnizacion)"
    }

}
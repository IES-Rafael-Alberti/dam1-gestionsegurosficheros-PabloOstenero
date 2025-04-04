package model
import java.time.LocalDate

class SeguroHogar : Seguro {

    private val metrosCuadrados: Int
    private val valorContenido: Double
    private val direccion: String
    private val anioConstruccion: Int

    private constructor(
        numPoliza: Int,
        dniTitular: String,
        importe: Double,
        metrosCuadrados: Int,
        valorContenido: Double,
        direccion: String,
        anioConstruccion: Int
    ) : super(numPoliza, dniTitular, importe) {
        this.metrosCuadrados = metrosCuadrados
        this.valorContenido = valorContenido
        this.direccion = direccion
        this.anioConstruccion = anioConstruccion
    }

    constructor(
        dniTitular: String,
        importe: Double,
        metrosCuadrados: Int,
        valorContenido: Double,
        direccion: String,
        anioConstruccion: Int
    ) : super(++numPolizasHogar, dniTitular, importe) {
        this.metrosCuadrados = metrosCuadrados
        this.valorContenido = valorContenido
        this.direccion = direccion
        this.anioConstruccion = anioConstruccion
    }

    companion object {
        internal var numPolizasHogar: Int = 100000

        const val PORCENTAJE_INCREMENTO_ANIOS = 0.02
        const val CICLO_ANIOS_INCREMENTO = 5

        fun crearSeguro(datos: List<String>): SeguroHogar {
                val numPoliza = datos[0].toInt()
                val dniTitular = datos[1]
                val importe = datos[2].toDouble()
                val metrosCuadrados = datos[3].toInt()
                val valorContenido = datos[4].toDouble()
                val direccion = datos[5]
                val anioConstruccion = datos[6].toInt()

                return SeguroHogar(numPoliza, dniTitular, importe, metrosCuadrados, valorContenido, direccion, anioConstruccion)

        }
    }

    /** Retorna el importe del año siguiente basándose en el interés que se pasa por parámetro,
     * sumándole un interés residual de 0.02% por cada 5 años de antiguedad del hogar */
    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val antiguedad = LocalDate.now().year - anioConstruccion
        val incremento = (antiguedad / CICLO_ANIOS_INCREMENTO) * PORCENTAJE_INCREMENTO_ANIOS

        return importe * (1 + interes + incremento)
    }

    override fun serializar(separador: String): String {
        return super.serializar(separador) +
                "$metrosCuadrados$separador$valorContenido$separador$direccion$separador$anioConstruccion$separador${tipoSeguro()}"
    }

    override fun toString(): String {
        return "Seguro Hogar(${super.toString()}, metrosCuadrados=$metrosCuadrados, valorContenido=$valorContenido, direccion=$direccion, anioConstruccion=$anioConstruccion)"
    }


}
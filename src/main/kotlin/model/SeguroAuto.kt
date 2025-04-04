package model

class SeguroAuto : Seguro {

    private val descripcion: String
    private val combustible: String
    private val tipoAuto: Auto
    private val cobertura: Cobertura
    private val asistenciaCarretera: Boolean
    private val numPartes: Int

    private constructor(
        numPoliza: Int,
        dniTitular: String,
        importe: Double,
        descripcion: String,
        combustible: String,
        tipoAuto: Auto,
        cobertura: Cobertura,
        asistenciaCarretera: Boolean,
        numPartes: Int
    ) : super(numPoliza, dniTitular, importe) {
        this.descripcion = descripcion
        this.combustible = combustible
        this.tipoAuto = tipoAuto
        this.cobertura = cobertura
        this.asistenciaCarretera = asistenciaCarretera
        this.numPartes = numPartes
    }

    constructor(
        dniTitular: String,
        importe: Double,
        descripcion: String,
        combustible: String,
        tipoAuto: Auto,
        cobertura: Cobertura,
        asistenciaCarretera: Boolean,
        numPartes: Int
    ) : super (++numPolizasAuto, dniTitular, importe) {
        this.descripcion = descripcion
        this.combustible = combustible
        this.tipoAuto = tipoAuto
        this.cobertura = cobertura
        this.asistenciaCarretera = asistenciaCarretera
        this.numPartes = numPartes
    }

    companion object {
        internal var numPolizasAuto: Int = 400000
        const val PORCENTAJE_INCREMENTO_PARTES = 2

        fun crearSeguro(datos: List<String>): SeguroAuto {
            val numPoliza = datos[0].toInt()
            val dniTitular = datos[1]
            val importe = datos[2].toDouble()
            val descripcion = datos[3]
            val combustible = datos[4]
            val tipoAuto = Auto.getAuto(datos[5])
            val cobertura = Cobertura.getCobertura(datos[6])
            val asistenciaCarretera = datos[7].toBoolean()
            val numPartes = datos[8].toInt()

            return SeguroAuto(numPoliza, dniTitular, importe, descripcion, combustible, tipoAuto, cobertura, asistenciaCarretera, numPartes)
        }
    }

    /**
     * Retorna el importe del año siguiente basándose en el interés que se pasa por parámetro, sumándole
     * un interés residual del 2% por cada parte declarado.
     */
    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val incremento = PORCENTAJE_INCREMENTO_PARTES * numPartes / 100.0

        return importe * (1 + interes + incremento)
    }

    override fun serializar(separador: String): String {
        return super.serializar(separador) + separador +
                "$descripcion$separador$combustible$separador$tipoAuto$separador$cobertura$separador$asistenciaCarretera$separador$numPartes$separador${tipoSeguro()}"
    }

    override fun toString(): String {
        return "Seguro Auto(${super.toString()}, descripcion=$descripcion, combustible=$combustible, tipoAuto=$tipoAuto, cobertura=$cobertura, asistenciaCarretera=$asistenciaCarretera, numPartes=$numPartes)"
    }

}
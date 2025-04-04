package model

abstract class Seguro(val numPoliza: Int, private val dniTitular: String, protected val importe: Double) : IExportable  {

    abstract fun calcularImporteAnioSiguiente(interes: Double): Double

    override fun serializar(separador: String): String {
        return "$numPoliza$separador$dniTitular$separador$importe"
    }

    /** Retorna el nombre de la clase usando this::class.simpleName y el operador elvis para
     * indicar al compilador que si simpleName es null se retorne el valor "Desconocido" */
    fun tipoSeguro(): String {
        return this::class.simpleName ?: "Desconocido"
    }

    override fun toString(): String {
        return "Seguro(numPoliza=$numPoliza, dniTitular=$dniTitular, importe=${"%.2f".format(importe)})"
    }

    override fun hashCode(): Int {
        return numPoliza.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Seguro) return false
        return numPoliza == other.numPoliza
    }
}
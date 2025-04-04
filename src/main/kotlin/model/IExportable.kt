package model

/** Representa que la clase que lo herede se puede exportar */
interface IExportable {

    /** Retorna una cadena de caracteres con los valores de los atributos de la clase separados
     * por el valor indicado en separador */
    fun serializar(separador: String = ";"): String
}
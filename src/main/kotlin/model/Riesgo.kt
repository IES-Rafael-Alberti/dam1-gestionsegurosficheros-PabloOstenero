package model

/** Representa los niveles de riesgo en los seguros de vida */
enum class Riesgo(val interesAplicado: Double) {
    BAJO(2.0),
    MEDIO(5.0),
    ALTO(10.0);

    companion object {
        /** Retorna el valor de la enumeración correspondiente a la cadena de caracteres que se pasa por argumento
         * o MEDIO si no existe. */
        fun getRiesgo(valor: String): Riesgo {
            return when (valor.trim().uppercase()) {
                "BAJO" -> BAJO
                "ALTO" -> ALTO
                else -> MEDIO
            }
        }
    }
}
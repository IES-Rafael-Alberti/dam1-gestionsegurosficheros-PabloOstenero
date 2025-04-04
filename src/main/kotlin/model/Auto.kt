package model

/** Representa el tipo de automóvil asegurado */
enum class Auto {
    COCHE, MOTO, CAMION;

    companion object {
        /** Retorna el valor de la enumeración correspondiente a la cadena de caracteres que se pasa por argumento
         * o COCHE si no existe. */
        fun getAuto(valor: String): Auto {
            return when (valor.trim().uppercase()) {
                "MOTO" -> MOTO
                "CAMION" -> CAMION
                else -> COCHE
            }
        }
    }
}
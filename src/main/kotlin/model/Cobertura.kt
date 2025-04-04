package model

/** Representa los tipos de coberturas de los seguros del automóvil */
enum class Cobertura(val desc: String) {
    TERCEROS("Terceros"),
    TERCEROS_AMPLIADO("Terceros +"),
    FRANQUICIA_200("Todo Riesgo con Franquicia de 200€"),
    FRANQUICIA_300("Todo Riesgo con Franquicia de 300€"),
    FRANQUICIA_400("Todo Riesgo con Franquicia de 400€"),
    FRANQUICIA_500("Todo Riesgo con Franquicia de 500€"),
    TODO_RIESGO("Todo Riesgo");

    companion object {
        /** Retorna el valor de la enumeración correspondiente a la cadena de caracteres que se pasa por argumento
         * o TERCEROS si no existe. */
        fun getCobertura(valor: String): Cobertura {
            return when (valor.trim().uppercase()) {
                "TERCEROS" -> TERCEROS
                "FRANQUICIA_200" -> FRANQUICIA_200
                "FRANQUICIA_300" -> FRANQUICIA_300
                "FRANQUICIA_400" -> FRANQUICIA_400
                "FRANQUICIA_500" -> FRANQUICIA_500
                "TODO_RIESGO" -> TODO_RIESGO
                else -> TERCEROS_AMPLIADO
            }
        }
    }
}
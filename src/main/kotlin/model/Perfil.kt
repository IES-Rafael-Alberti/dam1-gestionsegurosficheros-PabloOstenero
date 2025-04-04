package model

/** Representa los tipos de perfil que puede tener un usuario:
 * ADMIN: Puede gestionar usuarios y seguros.
 * GESTION: Puede gestionar seguros, pero no crear/eliminar usuarios.
 * CONSULTA: Solo puede ver información.*/
enum class Perfil {
    ADMIN, GESTION, CONSULTA;

    companion object {
        /** Retorna el valor de la enumeración correspondiente a la cadena de caracteres que se pasa por argumento
         * o CONSULTA si no existe. */
        fun getPerfil(valor: String): Perfil {
            return if (valor.trim().uppercase() == "ADMIN")
                ADMIN
            else {
                if (valor.trim().uppercase() == "GESTION")
                    GESTION
                else
                    CONSULTA
            }
        }
    }
}
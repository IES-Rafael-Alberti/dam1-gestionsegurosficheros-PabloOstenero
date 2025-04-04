package model

class Usuario(val nombre: String, clave: String, val perfil: Perfil): IExportable {

    var clave: String = clave
        private set

    companion object {
        fun crearUsuario(datos: List<String>): Usuario {
            val nombre = datos[0]
            val clave = datos[1]
            val perfil = Perfil.getPerfil(datos[2])

            return Usuario(nombre, clave, perfil)
        }
    }

    fun cambiarClave(nuevaClaveEncriptada: String) {
        clave = nuevaClaveEncriptada
    }

    override fun serializar(separador: String): String {
        return "$nombre$separador$clave$separador$perfil"
    }

    override fun hashCode(): Int {
        return nombre.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Usuario) return false
        return this.nombre == other.nombre
    }
}
package data
import model.Usuario
import model.Perfil

open class RepoUsuariosMem : IRepoUsuarios {
    protected val usuarios = mutableListOf<Usuario>()

    override fun agregar(usuario: Usuario): Boolean {
        if (buscar(usuario.nombre) != null) return false
        return usuarios.add(usuario)
    }

    override fun buscar(nombreUsuario: String): Usuario? {
        return usuarios.find { it.nombre == nombreUsuario }
    }

    override fun eliminar(usuario: Usuario): Boolean {
        return usuarios.remove(usuario)
    }

    override fun eliminar(nombreUsuario: String): Boolean {
        val usuario = buscar(nombreUsuario)
        return if (usuario != null) eliminar(usuario) else false
    }

    override fun obtenerTodos(): List<Usuario> = usuarios

    override fun obtener(perfil: Perfil): List<Usuario> {
        return usuarios.filter { it.perfil == perfil }
    }



    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        try {
            usuario.cambiarClave(nuevaClave)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}

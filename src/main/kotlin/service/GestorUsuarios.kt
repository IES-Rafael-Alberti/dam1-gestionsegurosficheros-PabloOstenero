package service

import data.IRepoUsuarios
import model.Perfil
import model.Usuario
import utils.IUtilSeguridad

class GestorUsuarios(
    private val repoUsuario: IRepoUsuarios,
    private val seguridad: IUtilSeguridad): IServUsuarios {

    /** Busca el usuario y verifica que la contraseña introducida sea válida.
     * del usuario. Si no, devuelve null. */
    override fun iniciarSesion(nombre: String, clave: String): Perfil? {
        val usuario = repoUsuario.buscar(nombre)

        return if (usuario != null && seguridad.verificarClave(clave, usuario.clave)) {
            usuario.perfil
        } else {
            null
        }
    }

    override fun agregarUsuario(nombre: String, clave: String, perfil: Perfil): Boolean {
        val usuario = Usuario(nombre, seguridad.encriptarClave(clave), perfil)

        return repoUsuario.agregar(usuario)
    }

    override fun eliminarUsuario(nombre: String): Boolean {
        return repoUsuario.eliminar(nombre)
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        return repoUsuario.cambiarClave(usuario, seguridad.encriptarClave(nuevaClave))
    }

    override fun buscarUsuario(nombre: String): Usuario? {
        return repoUsuario.buscar(nombre)
    }

    override fun consultarTodos(): List<Usuario> {
        return repoUsuario.obtenerTodos()
    }

    override fun consultarPorPerfil(perfil: Perfil): List<Usuario> {
        return repoUsuario.obtenerTodos().filter { it.perfil == perfil }
    }
}
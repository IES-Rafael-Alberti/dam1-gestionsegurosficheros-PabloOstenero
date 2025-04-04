package data
import model.Usuario
import utils.IUtilFicheros

class RepoUsuariosFich(
    private val rutaArchivo: String,
    private val fich: IUtilFicheros
) : RepoUsuariosMem(), ICargarUsuariosIniciales {


    override fun agregar(usuario: Usuario): Boolean {
        if (super.agregar(usuario)) {
            return fich.agregarLinea(rutaArchivo, usuario.serializar())
        }
        return false
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        usuario.cambiarClave(nuevaClave)

        return fich.escribirArchivo(rutaArchivo, usuarios)
    }

    override fun eliminar(usuario: Usuario): Boolean {
        if (fich.escribirArchivo(rutaArchivo, usuarios.filter { it != usuario })) {
            return super.eliminar(usuario)
        }

        return false
    }

    override fun eliminar(nombreUsuario: String): Boolean {
        val usuario = buscar(nombreUsuario)

        if (usuario != null && fich.escribirArchivo(rutaArchivo, usuarios)) {
            return super.eliminar(nombreUsuario)
        }

        return false
    }

    override fun cargarUsuarios(): Boolean {
        val lineas = fich.leerArchivo(rutaArchivo)

        if (lineas.isNotEmpty()) {
            lineas.forEach { linea ->
                val datos = linea.split(";")
                val usuario = Usuario.crearUsuario(datos)
                usuarios.add(usuario)
            }
            return usuarios.isNotEmpty()
        }
        return false
    }
}

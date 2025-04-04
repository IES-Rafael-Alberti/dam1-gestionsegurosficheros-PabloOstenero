package app

import model.Perfil
import service.IServUsuarios
import ui.IEntradaSalida
import utils.IUtilFicheros

/**
 * Clase responsable del control de acceso de usuarios: alta inicial, inicio de sesión
 * y recuperación del perfil. Su objetivo es asegurar que al menos exista un usuario
 * en el sistema antes de acceder a la aplicación.
 *
 * Esta clase encapsula toda la lógica relacionada con la autenticación de usuarios,
 * separando así la responsabilidad del acceso del resto de la lógica de negocio.
 *
 * Utiliza inyección de dependencias (DIP) para recibir los servicios necesarios:
 * - La ruta del archivo de usuarios
 * - El gestor de usuarios para registrar o validar credenciales
 * - La interfaz de entrada/salida para interactuar con el usuario
 * - La utilidad de ficheros para comprobar la existencia y contenido del fichero
 *
 * @property rutaArchivo Ruta del archivo donde se encuentran los usuarios registrados.
 * @property gestorUsuarios Servicio encargado de la gestión de usuarios (login, alta...).
 * @property ui Interfaz para mostrar mensajes y recoger entradas del usuario.
 * @property ficheros Utilidad para operar con ficheros (leer, comprobar existencia...).
 */
class ControlAcceso(private val rutaArchivo: String,
    private val gestorUsuarios: IServUsuarios,
    private val ui: IEntradaSalida,
    private val ficheros: IUtilFicheros)
{

    /**
     * Inicia el proceso de autenticación del sistema.
     *
     * Primero verifica si hay usuarios registrados. Si el archivo está vacío o no existe,
     * ofrece al usuario la posibilidad de crear un usuario ADMIN inicial.
     *
     * A continuación, solicita credenciales de acceso en un bucle hasta que sean válidas
     * o el usuario decida cancelar el proceso.
     *
     * @return Un par (nombreUsuario, perfil) si el acceso fue exitoso, o `null` si el usuario cancela el acceso.
     */
    fun autenticar(): Pair<String, Perfil>? {
        if (!verificarFicheroUsuarios()) {
            ui.mostrarError("Debe existir al menos un usuario registrado para continuar.")
            return null
        }
        return iniciarSesion()
    }

    /**
     * Verifica si el archivo de usuarios existe y contiene al menos un usuario registrado.
     *
     * Si el fichero no existe o está vacío, se informa al usuario y se le pregunta si desea
     * registrar un nuevo usuario con perfil ADMIN.
     *
     * Este metodo se asegura de que siempre haya al menos un usuario en el sistema.
     *
     * @return `true` si el proceso puede continuar (hay al menos un usuario),
     *         `false` si el usuario cancela la creación inicial o ocurre un error.
     */
    private fun verificarFicheroUsuarios(): Boolean {
        if (!ficheros.existeFichero(rutaArchivo)) {
            ui.mostrarError("El fichero de usuarios no existe.")
            val respuesta = ui.pedirInfo("¿Desea crear un usuario ADMIN inicial? (s/n)").trim().lowercase()

            return respuesta == "s"

        } else {
            val lineas = ficheros.leerArchivo(rutaArchivo)

            if (lineas.isEmpty()) {
                ui.mostrarError("El fichero de usuarios está vacío.")
                val respuesta = ui.pedirInfo("¿Desea crear un usuario ADMIN inicial? (s/n)").trim().lowercase()

                return respuesta == "s"
            }
        }

        return true
    }

    /**
     * Solicita al usuario sus credenciales (usuario y contraseña) en un bucle hasta
     * que sean válidas o el usuario decida cancelar.
     *
     * Si la autenticación es exitosa, se retorna el nombre del usuario y su perfil.
     *
     * @return Un par (nombreUsuario, perfil) si las credenciales son correctas,
     *         o `null` si el usuario decide no continuar.
     */
    private fun iniciarSesion(): Pair<String, Perfil>? {
        while (true) {
            val nombre = ui.pedirInfo("Usuario (o escriba 'cancelar' para salir):")

            if (nombre.equals("cancelar", ignoreCase = true)) return null

            val clave = ui.pedirInfo("Clave:")
            val perfil = gestorUsuarios.iniciarSesion(nombre, clave)

            if (perfil != null) {
                ui.mostrar("Acceso concedido. ¡Bienvenido $nombre!")

                return Pair(nombre, perfil)

            } else {
                ui.mostrarError("Credenciales incorrectas. Intente de nuevo.")
            }
        }
    }
}
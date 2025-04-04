import app.CargadorInicial
import data.*
import ui.Consola
import utils.Ficheros

fun main() {

    // Crear dos variables con las rutas de los archivos de texto donde se almacenan los usuarios y seguros.
    // Estos ficheros se usarán solo si el programa se ejecuta en modo de almacenamiento persistente.
    val rutaUsuarios = "res/Usuarios.txt"
    val rutaSeguros = "res/Seguros.txt"

    // Instanciamos los componentes base del sistema: la interfaz de usuario, el gestor de ficheros y el módulo de seguridad.
    // Estos objetos serán inyectados en los diferentes servicios y utilidades a lo largo del programa.
    val con = Consola()
    val fichero = Ficheros(con)

    // Limpiamos la pantalla antes de comenzar, para que la interfaz esté despejada al usuario.
    con.limpiarPantalla()

    // Preguntamos al usuario si desea iniciar en modo simulación.
    // En modo simulación los datos no se guardarán en archivos, solo estarán en memoria durante la ejecución.
    val opcion = con.pedirEntero(" 1. Modo simulación\n 2. Modo almacenamiento",
        "Valor incorrecto",
        "Ha ocurrido un error al convertir el dato") { it in 1..2 }

    // Declaramos los repositorios de usuarios y seguros.
    // Se asignarán más abajo dependiendo del modo elegido por el usuario.

    var repoUsuarios: IRepoUsuarios
    var repoSeguros: IRepoSeguros


    // Si se ha elegido modo simulación, se usan repositorios en memoria.
    // Si se ha elegido almacenamiento persistente, se instancian los repositorios que usan ficheros.
    // Además, creamos una instancia del cargador inicial de información y lanzamos la carga desde los ficheros.

    // Se crean los servicios de lógica de negocio, inyectando los repositorios y el componente de seguridad.


    // Se inicia el proceso de autenticación. Se comprueba si hay usuarios en el sistema y se pide login.
    // Si no hay usuarios, se permite crear un usuario ADMIN inicial.


    // Si el login fue exitoso (no es null), se inicia el menú correspondiente al perfil del usuario autenticado.
    // Se lanza el menú principal, **iniciarMenu(0)**, pasándole toda la información necesaria.
    if (opcion == 1) {
        repoUsuarios = RepoUsuariosMem()
        repoSeguros = RepoSegurosMem()

    }
    else {
        repoUsuarios = RepoUsuariosFich(rutaUsuarios, fichero)
        repoSeguros = RepoSegurosFich(rutaSeguros, fichero)
        val cargadorInicial = CargadorInicial(con, repoUsuarios, repoSeguros)
    }


}
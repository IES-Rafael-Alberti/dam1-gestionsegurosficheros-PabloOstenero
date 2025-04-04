package app

import model.Auto
import model.Cobertura
import model.Perfil
import model.Riesgo
import service.IServSeguros
import service.IServUsuarios
import ui.IEntradaSalida
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Clase encargada de gestionar el flujo de menús y opciones de la aplicación,
 * mostrando las acciones disponibles según el perfil del usuario autenticado.
 *
 * @property nombreUsuario Nombre del usuario que ha iniciado sesión.
 * @property perfilUsuario Perfil del usuario: admin, gestion o consulta.
 * @property ui Interfaz de usuario.
 * @property gestorUsuarios Servicio de operaciones sobre usuarios.
 * @property gestorSeguros Servicio de operaciones sobre seguros.
 */
class GestorMenu(private val nombreUsuario: String,
                 private val perfilUsuario: String,
                 private val ui: IEntradaSalida,
                 private val gestorUsuarios: IServUsuarios,
                 private val gestorSeguros: IServSeguros
)
{


    /**
     * Inicia un menú según el índice correspondiente al perfil actual.
     *
     * @param indice Índice del menú que se desea mostrar (0 = principal).
     */
    fun iniciarMenu(indice: Int = 0) {
        val (opciones, acciones) = ConfiguracionesApp.obtenerMenuYAcciones(perfilUsuario, indice)
        ejecutarMenu(opciones, acciones)
    }

    /**
     * Formatea el menú en forma numerada.
     */
    private fun formatearMenu(opciones: List<String>): String {
        var cadena = ""
        opciones.forEachIndexed { index, opcion ->
            cadena += "${index + 1}. $opcion\n"
        }
        return cadena
    }

    /**
     * Muestra el menú limpiando pantalla y mostrando las opciones numeradas.
     */
    private fun mostrarMenu(opciones: List<String>) {
        ui.limpiarPantalla()
        ui.mostrar(formatearMenu(opciones), salto = false)
    }

    /**
     * Ejecuta el menú interactivo.
     *
     * @param opciones Lista de opciones que se mostrarán al usuario.
     * @param ejecutar Mapa de funciones por número de opción.
     */
    private fun ejecutarMenu(opciones: List<String>, ejecutar: Map<Int, (GestorMenu) -> Boolean>) {
        do {
            mostrarMenu(opciones)
            val opcion = ui.pedirInfo("Elige opción > ").toIntOrNull()
            if (opcion != null && opcion in 1..opciones.size) {
                // Buscar en el mapa las acciones a ejecutar en la opción de menú seleccionada
                val accion = ejecutar[opcion]
                // Si la accion ejecutada del menú retorna true, debe salir del menú
                if (accion != null && accion(this)) return
            }
            else {
                ui.mostrarError("Opción no válida!")
            }
        } while (true)
    }

    /** Crea un nuevo usuario solicitando los datos necesarios al usuario */
    fun nuevoUsuario() {
        try {
            val nombre = ui.pedirInfo("Ingrese nombre de usuario:")
            val clave = ui.pedirInfo("Ingrese clave:")
            val perfilStr = ui.pedirInfo("Ingrese perfil (ADMIN, GESTION, CONSULTA):").uppercase()
            val perfil = Perfil.getPerfil(perfilStr)

            if (gestorUsuarios.agregarUsuario(nombre, clave, perfil)) {
                ui.mostrar("Usuario $nombre creado correctamente.")

            } else {
                ui.mostrarError("No se pudo crear el usuario $nombre.")
            }

        } catch (e: IllegalArgumentException) {
            ui.mostrarError("Perfil inválido. Use ADMIN, GESTION o CONSULTA.")
        }

    }

    /** Elimina un usuario si existe */
    fun eliminarUsuario() {
        val nombre = ui.pedirInfo("Ingrese nombre de usuario a eliminar:")

        if (gestorUsuarios.eliminarUsuario(nombre)) {
            ui.mostrar("Usuario $nombre eliminado correctamente.")

        } else {
            ui.mostrarError("No se encontró o no se pudo eliminar el usuario $nombre.")
        }

    }

    /** Cambia la contraseña del usuario actual */
    fun cambiarClaveUsuario() {
        val nuevaClave = ui.pedirInfo("Ingrese nueva clave:")
        val usuario = gestorUsuarios.buscarUsuario(nombreUsuario)

        if (usuario == null) {
            ui.mostrarError("Usuario $nombreUsuario no encontrado.")

        } else {
            if (gestorUsuarios.cambiarClave(usuario, nuevaClave)) {
                ui.mostrar("Contraseña actualizada correctamente.")

            } else {
                ui.mostrarError("Error al actualizar la contraseña.")
            }
        }
    }

    /**
     * Mostrar la lista de usuarios (Todos o filstrados por un perfil)
     */
    fun consultarUsuarios() {
        val perfiles = Perfil.entries.joinToString()
        val filtro = ui.pedirInfo("¿Desea filtrar por perfil? (s/n):").lowercase()

        val usuarios = if (filtro == "s") {
            val perfilStr = ui.pedirInfo("Ingrese perfil a filtrar ($perfiles):").uppercase()

            try {
                val perfil = Perfil.getPerfil(perfilStr)
                gestorUsuarios.consultarPorPerfil(perfil)

            } catch (e: IllegalArgumentException) {
                ui.mostrarError("Perfil inválido. Se mostrarán todos los usuarios.")
                gestorUsuarios.consultarTodos()
            }

        } else {
            gestorUsuarios.consultarTodos()
        }
        if (usuarios.isEmpty()) {
            ui.mostrar("No hay usuarios registrados.")

        } else {
            usuarios.forEach { ui.mostrar(it.toString()) }
        }    }

    /**
     * Solicita al usuario un DNI y verifica que tenga el formato correcto: 8 dígitos seguidos de una letra.
     *
     * @return El DNI introducido en mayúsculas.
     */
    private fun pedirDni(): String {
        val regex = Regex("^[0-9]{8}[A-Z]\$")

        while (true) {
            val dni = ui.pedirInfo("Ingrese DNI (8 dígitos y una letra):").uppercase()

            if (dni.matches(regex)) return dni

            ui.mostrarError("DNI inválido. Debe contener 8 dígitos y una letra.")
        }
    }

    /**
     * Solicita al usuario un importe positivo, usado para los seguros.
     *
     * @return El valor introducido como `Double` si es válido.
     */
    private fun pedirImporte(): Double {
        while (true) {
            val input = ui.pedirInfo("Ingrese un importe positivo: ")
            val importe = input.toDoubleOrNull()

            if (importe != null && importe > 0) return importe

            ui.mostrarError("Importe inválido. Intente nuevamente.")
        }    }

    /** Crea un nuevo seguro de hogar solicitando los datos al usuario */
    fun contratarSeguroHogar() {
        try {
            val dni = pedirDni()
            val importe = pedirImporte()

            val metrosCuadrados = ui.pedirEntero("Escribe los metros cuadrados del hogar: ",
                "La casa debe medir mas de 0 metros cuadrados!!!",
                "No se ha podido hacer la conversión") { it > 0 }

            val valorContenido = ui.pedirDouble("Escribe el valor contenido: ",
                "Debe ser mayor que 0",
                "No se ha podido hacer la conversión") { it > 0 }

            val direccion = ui.pedirInfo("Dame la dirección del hogar")

            val anioConstruccion = ui.pedirEntero("Escribe el año de construcción de la casa",
                "La casa no puede estar creada en el futuro!!!",
                "No se ha podido hacer la conversión") { it <= LocalDate.now().year }

            gestorSeguros.contratarSeguroHogar(dni, importe, metrosCuadrados, valorContenido, direccion, anioConstruccion)

        } catch (e: Exception) {
            ui.mostrarError(e.message.toString())
        }
    }

    /** Crea un nuevo seguro de auto solicitando los datos al usuario */
    fun contratarSeguroAuto() {
        val tiposAutos = Auto.entries.joinToString()
        val coberturas = Cobertura.entries.joinToString()

        try {
            val dni = pedirDni()
            val importe = pedirImporte()
            val descripcion = ui.pedirInfo("Escribe una descripción del coche: ")
            val combustible = ui.pedirInfo("Escribe el tipo de combustible que tiene el auto: ")
            val tipoAutoStr = ui.pedirInfo("Diga el tipo de auto que es ($tiposAutos):")
            val tipoAuto = Auto.getAuto(tipoAutoStr)
            val coberturaStr = ui.pedirInfo("Diga la cobertura que tiene el contrato ($coberturas)")
            val cobertura = Cobertura.getCobertura(coberturaStr)
            val asistenciaCarreteraStr = ui.pedirInfo("Tiene asistencia a carretera? (s/n)").trim().lowercase()
            val asistenciaCarretera = asistenciaCarreteraStr == "s"
            val numPartes = ui.pedirEntero("Cuantas partes tiene el seguro",
                "No debe tener menos de 1 parte",
                "No se ha podido convertir") { it > 0 }

            gestorSeguros.contratarSeguroAuto(dni, importe, descripcion, combustible, tipoAuto, cobertura, asistenciaCarretera, numPartes)

        } catch (e: Exception) {
            ui.mostrarError(e.message.toString())
        }
    }

    /** Crea un nuevo seguro de vida solicitando los datos al usuario */
    fun contratarSeguroVida() {
        val nivelesRiesgo = Riesgo.entries.joinToString()

        try {
            val dni = pedirDni()
            val importe = pedirImporte()
            val fechaNacimientoStr = ui.pedirInfo("Dime la fecha de tu nacimiento (dd/MM/yyyy): ")
            val fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val nivelRiesgoStr = ui.pedirInfo("Escribe el nivel de riesgo a contratar ($nivelesRiesgo): ")
            val nivelRiesgo = Riesgo.getRiesgo(nivelRiesgoStr)
            val indemnizacion = ui.pedirDouble("Dime la indemnización a recibir: ",
                "Debe ser mayor que 0!!!",
                "Ha ocurrido un error al hacer la conversión") { it > 0 }

            gestorSeguros.contratarSeguroVida(dni, importe, fechaNacimiento, nivelRiesgo, indemnizacion)

        } catch (e: Exception) {
            ui.mostrarError(e.message.toString())
        }
    }

    /** Elimina un seguro si existe por su número de póliza */
    fun eliminarSeguro() {
        try {
            ui.pedirEntero("Dame el número de póliza del seguro a borrar: ",
                "Número de póliza incorrecto",
                "Ha ocurrido un error al hacer la conversión") { gestorSeguros.eliminarSeguro(it) }

        } catch (e: Exception) {
            ui.mostrarError(e.message.toString())
        }
    }

    /** Muestra todos los seguros existentes */
    fun consultarSeguros() {
        gestorSeguros.consultarTodos()
    }

    /** Muestra todos los seguros de tipo hogar */
    fun consultarSegurosHogar() {
        gestorSeguros.consultarPorTipo("HOGAR")
    }

    /** Muestra todos los seguros de tipo auto */
    fun consultarSegurosAuto() {
        gestorSeguros.consultarPorTipo("AUTO")
    }

    /** Muestra todos los seguros de tipo vida */
    fun consultarSegurosVida() {
        gestorSeguros.consultarPorTipo("VIDA")
    }

}
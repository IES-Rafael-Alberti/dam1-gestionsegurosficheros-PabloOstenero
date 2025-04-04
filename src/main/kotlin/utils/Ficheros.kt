package utils

import model.IExportable
import ui.IEntradaSalida
import java.io.File
import java.io.IOException

class Ficheros(private val ui: IEntradaSalida): IUtilFicheros {
    override fun leerArchivo(ruta: String): List<String> {
        return try {
            val file = File(ruta)

            if (!file.exists()) {
                ui.mostrarError("El fichero $ruta no existe.")
                emptyList()

            } else {
                file.readLines()
            }
        } catch (e: IOException) {
            ui.mostrarError("Error al leer el fichero $ruta: ${e.message}")
            emptyList()
        }
    }

    override fun agregarLinea(ruta: String, linea: String): Boolean {
        return try {
            val file = File(ruta)

            file.appendText("$linea\n")
            true

        } catch (e: IOException) {
            ui.mostrarError("Error al agregar línea al fichero $ruta: ${e.message}")
            false
        }
    }

    override fun <T : IExportable> escribirArchivo(ruta: String, elementos: List<T>): Boolean {
        return try {
            val file = File(ruta)

            val contenido = elementos.joinToString(separator = "\n") { it.serializar() }
            file.writeText(contenido)
            true

        } catch (e: IOException) {
            ui.mostrarError("Error al escribir en el fichero $ruta: ${e.message}")
            false
        }
    }

    override fun existeFichero(ruta: String): Boolean {
        return File(ruta).let { it.exists() && it.isFile }
    }

    override fun existeDirectorio(ruta: String): Boolean {
        return File(ruta).let { it.exists() && it.isDirectory }
    }
}
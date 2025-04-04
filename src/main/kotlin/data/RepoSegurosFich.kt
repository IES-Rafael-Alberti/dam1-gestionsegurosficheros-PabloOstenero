package data
import model.*
import utils.IUtilFicheros

class RepoSegurosFich(
    private val rutaArchivo: String,
    private val fich: IUtilFicheros
) : RepoSegurosMem(), ICargarSegurosIniciales {

    override fun agregar(seguro: Seguro): Boolean {
        if (fich.agregarLinea(rutaArchivo, seguro.serializar())) {
            return super.agregar(seguro)
        }

        return false
    }

    override fun eliminar(seguro: Seguro): Boolean {
        if (fich.escribirArchivo(rutaArchivo, seguros.filter { it != seguro })) {
            return super.eliminar(seguro)
        }
        return false
    }

    override fun cargarSeguros(mapa: Map<String, (List<String>) -> Seguro>): Boolean {
        val lineas = fich.leerArchivo(rutaArchivo)

        if (lineas.isNotEmpty()) {
            lineas.forEach { linea ->
                val datos = linea.split(";")
                val tipo = datos.last()
                val crearSeguro = mapa[tipo]

                if (crearSeguro != null) {
                    val seguro = crearSeguro(datos.dropLast(1))
                    seguros.add(seguro)
                }
            }
            actualizarContadores(seguros)
            return seguros.isNotEmpty()
        }
        return false
    }

    private fun actualizarContadores(seguros: List<Seguro>) {
        // Actualizar los contadores de polizas del companion object según el tipo de seguro
        val maxHogar = seguros.filter { it.tipoSeguro() == "SeguroHogar" }.maxOfOrNull { it.numPoliza }
        val maxAuto = seguros.filter { it.tipoSeguro() == "SeguroAuto" }.maxOfOrNull { it.numPoliza }
        val maxVida = seguros.filter { it.tipoSeguro() == "SeguroVida" }.maxOfOrNull { it.numPoliza }

        if (maxHogar != null) SeguroHogar.numPolizasHogar = maxHogar
        if (maxAuto != null) SeguroAuto.numPolizasAuto = maxAuto
        if (maxVida != null) SeguroVida.numPolizasVida = maxVida
    }
}
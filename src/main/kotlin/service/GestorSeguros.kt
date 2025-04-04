package service

import data.IRepoSeguros
import model.*
import java.time.LocalDate

class GestorSeguros(private val repoSeguros: IRepoSeguros): IServSeguros {

    override fun contratarSeguroHogar(
        dniTitular: String,
        importe: Double,
        metrosCuadrados: Int,
        valorContenido: Double,
        direccion: String,
        anioConstruccion: Int
    ): Boolean {
        return repoSeguros.agregar(SeguroHogar(dniTitular, importe, metrosCuadrados, valorContenido, direccion, anioConstruccion))
    }

    override fun contratarSeguroAuto(
        dniTitular: String,
        importe: Double,
        descripcion: String,
        combustible: String,
        tipoAuto: Auto,
        cobertura: Cobertura,
        asistenciaCarretera: Boolean,
        numPartes: Int
    ): Boolean {
        return repoSeguros.agregar(SeguroAuto(dniTitular, importe, descripcion, combustible, tipoAuto, cobertura, asistenciaCarretera, numPartes))
    }

    override fun contratarSeguroVida(
        dniTitular: String,
        importe: Double,
        fechaNacimiento: LocalDate,
        nivelRiesgo: Riesgo,
        indemnizacion: Double
    ): Boolean {
        return repoSeguros.agregar(SeguroVida(dniTitular, importe, fechaNacimiento, nivelRiesgo, indemnizacion))
    }

    override fun eliminarSeguro(numPoliza: Int): Boolean {
        return repoSeguros.eliminar(numPoliza)
    }

    override fun consultarTodos(): List<Seguro> {
        return repoSeguros.obtenerTodos()
    }

    override fun consultarPorTipo(tipoSeguro: String): List<Seguro> {
        return repoSeguros.obtener(tipoSeguro)
    }
}
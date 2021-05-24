package br.com.zupacademy.valeria.chavePix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {

    fun findByValChave(valChavePix: String): Optional<ChavePix>

    fun findByIdAndClienteId(id: Long, clienteId: String) : Optional<ChavePix>

}
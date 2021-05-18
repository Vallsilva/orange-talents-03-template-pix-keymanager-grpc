package br.com.zupacademy.valeria.chavePix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ClienteRepository : JpaRepository<ChavePix, Long> {

    fun findByValChave(valChavePix: String): Optional<ChavePix>
}
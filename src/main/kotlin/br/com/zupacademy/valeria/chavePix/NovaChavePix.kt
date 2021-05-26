package br.com.zupacademy.valeria.chavePix

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Introspected
data class NovaChavePix(@field:NotNull val tipoChave: TipoChave?,
                        @field:NotBlank var valChave: String?,
                        @field:NotBlank val clienteId: String?,
                        @field:NotNull val tipo: TipoConta?) {

    fun toModel(clienteResponse: ClienteResponse): ChavePix{
         return ChavePix(
            tipoChave = this.tipoChave!!,
            valChave = this.valChave!!,
             cpf= clienteResponse.titular.cpf,
            clienteId = this.clienteId!!,
            tipo = clienteResponse.tipo
        )
    }
}

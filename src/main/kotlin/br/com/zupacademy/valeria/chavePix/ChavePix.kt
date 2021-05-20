package br.com.zupacademy.valeria.chavePix

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Entity
@ValidadorChavePix
class ChavePix (@field:NotNull val tipoChave: TipoChave,
                @field:NotBlank val valChave: String,
                @field:NotBlank val cpf: String,
                @field:NotBlank val clienteId: String,
                @field:NotBlank val tipo: String)
{



    @Id @GeneratedValue
    val id: Long = 0

}
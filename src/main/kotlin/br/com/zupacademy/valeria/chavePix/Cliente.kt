package br.com.zupacademy.valeria.chavePix

import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank


@Embeddable
class Cliente(@field:NotBlank val cpf: String,
              @field:NotBlank val clienteId: String,
              @field:NotBlank val tipo: String) {
}
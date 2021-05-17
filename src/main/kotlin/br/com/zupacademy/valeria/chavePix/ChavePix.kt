package br.com.zupacademy.valeria.chavePix

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ChavePix (val tipoChave: TipoChave,
                val valChave: String,
                val cpf: String,
                val clienteId: String,
                val tipo: String)
{



    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}
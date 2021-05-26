package br.com.zupacademy.valeria.chavePix

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank


@Entity //Colocar validações bean validation
class ChavePix (val tipoChave: TipoChave,
                var valChave: String,
                val cpf: String,
                val clienteId: String,
                val tipo: String)
{
    @Id @GeneratedValue
    val id: Long? = null


}


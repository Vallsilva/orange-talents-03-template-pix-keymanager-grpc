package br.com.zupacademy.valeria.chavePix

import javax.persistence.*

//Criar uma validação para o tipo de chave usando a chave recebida
@Entity
class ChavePix (val tipoChave: TipoChave,
                val valChave: String,
                val cpf: String,
                val clienteId: String,
                val tipo: String)
{



    @Id @GeneratedValue
    val id: Long = 0

}
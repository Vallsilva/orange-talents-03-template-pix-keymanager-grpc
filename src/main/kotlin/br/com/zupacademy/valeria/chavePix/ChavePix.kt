package br.com.zupacademy.valeria.chavePix

import javax.persistence.*

@Entity
class ChavePix (val tipoChave: TipoChave,
                val valChave: String,
                //@Embedded val conta: Cliente)
)
{



    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}
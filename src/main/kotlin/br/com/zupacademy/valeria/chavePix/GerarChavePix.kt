package br.com.zupacademy.valeria.chavePix

import java.lang.Exception
import java.util.*

class GerarChavePix {

    companion object{
        fun gera(tipoChave: TipoChave, valChave: String) : String{
            if (tipoChave != TipoChave.RANDOM && valChave.isBlank())
                throw Exception("Valor da chave obrigatorio")

            if (tipoChave == TipoChave.RANDOM)
                return UUID.randomUUID().toString()

            return valChave
        }
    }
}
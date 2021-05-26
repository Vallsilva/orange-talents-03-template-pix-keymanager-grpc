package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.chavePix.consultaExterna.ComunicacaoChavePixBCB
import br.com.zupacademy.valeria.chavePix.consultaExterna.ConsultaErpItau
import br.com.zupacademy.valeria.handle.exception.ChavePixExistenteException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid

@Singleton
@Validated
class ChavePixService (@Inject val chavePixRepository: ChavePixRepository,
                       @Inject val clienteConsulta: ConsultaErpItau,
                       @Inject val clientBCB: ComunicacaoChavePixBCB
){

    fun registra(@Valid novaChavePix: NovaChavePix): ChavePix {

        val clienteResponse = clienteConsulta.consulta(novaChavePix.clienteId!!, novaChavePix.tipo!!.name)

        val chavePix = novaChavePix.toModel(clienteResponse)

        //criar anotação para validar isso
        if (chavePixRepository.findByValChave(chavePix.valChave).isPresent) {
            throw ChavePixExistenteException("Chave Pix ja cadastrada caralho!")

        }
        //criar anotação para validar isso também
        if (clientBCB.consulta(chavePix.valChave) != null) {
            throw ChavePixExistenteException("A chave Pix ja esta cadastrada no Banco Central do Brasil")
        }
        val chavePixBCBResponse = clientBCB.cadastra(
            ChavePixBBCRequest(
                keyType = chavePix.tipoChave,
                key = if (chavePix.tipoChave == TipoChave.RANDOM) "" else chavePix.valChave,
                bankAccount = BankAccount(clienteResponse),
                owner = Owner(clienteResponse)
            )
        )
        chavePix.valChave = chavePixBCBResponse.key

        return chavePixRepository.save(chavePix)
    }

}
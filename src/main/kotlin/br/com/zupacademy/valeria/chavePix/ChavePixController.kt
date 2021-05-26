package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.*
import br.com.zupacademy.valeria.TipoChave.*
import br.com.zupacademy.valeria.TipoConta.TIPO_CONTA_DESCONHECIDO
import br.com.zupacademy.valeria.chavePix.consultaExterna.ComunicacaoChavePixBCB
import br.com.zupacademy.valeria.chavePix.consultaExterna.ConsultaErpItau
import br.com.zupacademy.valeria.handle.ErrorHandler
import br.com.zupacademy.valeria.handle.exception.ChavePixExistenteException
import br.com.zupacademy.valeria.handle.exception.ChavePixInvalidaException
import br.com.zupacademy.valeria.handle.exception.ChavePixNaoEncontradaException
import br.com.zupacademy.valeria.handle.exception.ChavePixOutroDonoException
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Validator

@ErrorHandler
@Singleton
class ChavePixController(
    @Inject val chavePixRepository: ChavePixRepository,
    @Inject val clienteConsulta: ConsultaErpItau,
    @Inject val clientBCB: ComunicacaoChavePixBCB,
    @Inject val chavePixService: ChavePixService
) : KeyManagerPixServiceGrpc.KeyManagerPixServiceImplBase(){


    override fun cadastrarChavePix(
        request: KeyManagerPixRequest,
        responseObserver: StreamObserver<KeyManagerPixReply>
    ) {
        val chavePix = chavePixService.registra(request.toModel())

        val response = KeyManagerPixReply.newBuilder().setIdPix(chavePix.id!!).setValChave(chavePix.valChave).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun apagaChavePix(
        request: KeyRemoveRequest,
        responseObserver: StreamObserver<KeyRemoveReply>
    ) {

        if (!chavePixRepository.existsById(request.pixId.toLong())){

            throw ChavePixNaoEncontradaException("A chave informada não foi encontrada na base de dados!")
            return
        }

        if (chavePixRepository.findByIdAndClienteId(request.pixId.toLong(), request.clienteId).isEmpty){

            throw ChavePixOutroDonoException("A chave pix não pode ser excluida por outro usuário que não seja seu dono!")
            return
        }

        val chavePix = chavePixRepository.findById(request.pixId.toLong()).get()

        val clienteBCBConsulta = clientBCB.consulta(chavePix.valChave)
        val deletePixKeyResponse = clientBCB.deleta(chavePix.valChave, DeletePixKeyRequest(clienteBCBConsulta!!.key, clienteBCBConsulta.bankAccount.participant))

        chavePixRepository.deleteById(request.pixId.toLong())

        val response =  KeyRemoveReply.newBuilder().setClienteId(request.clienteId).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
        return
    }

}

private fun KeyManagerPixRequest.toModel(): NovaChavePix {

    return NovaChavePix(tipoChave = if (this.tipoChave == TIPO_CHAVE_DESCONHECIDO) null else TipoChave.valueOf(this.tipoChave.toString()),
        valChave = this.valChave,
        clienteId = this.clienteId,
        tipo = if (this.tipo == TIPO_CONTA_DESCONHECIDO) null else TipoConta.valueOf(this.tipo.toString()))
}

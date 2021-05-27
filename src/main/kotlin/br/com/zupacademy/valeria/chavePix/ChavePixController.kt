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
        val chavePix = chavePixService.deleta(request)

        val response =  KeyRemoveReply.newBuilder().setClienteId(chavePix.clienteId).build()

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

package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.KeyManagerPixRemoveGrpc
import br.com.zupacademy.valeria.KeyRemoveReply
import br.com.zupacademy.valeria.KeyRemoveRequest
import br.com.zupacademy.valeria.handle.ErrorHandler
import br.com.zupacademy.valeria.handle.exception.ChavePixNaoEncontradaException
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class DeletaChaveController (val chavePixRepository: ChavePixRepository) : KeyManagerPixRemoveGrpc.KeyManagerPixRemoveImplBase(){

    override fun apagaChavePix(
        request: KeyRemoveRequest,
        responseObserver: StreamObserver<KeyRemoveReply>
    ) {

        if (!chavePixRepository.existsById(request.pixId.toLong())){

            throw ChavePixNaoEncontradaException("A chave informada não foi encontrada na base de dados!")
            return
        }

        chavePixRepository.deleteById(request.pixId.toLong())
        val response =  KeyRemoveReply.newBuilder().setClienteId(request.clienteId).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
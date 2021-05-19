package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.KeyManagerPixRemoveGrpc
import br.com.zupacademy.valeria.KeyRemoveReply
import br.com.zupacademy.valeria.KeyRemoveRequest
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class DeletaChaveController (val chavePixRepository: ChavePixRepository) : KeyManagerPixRemoveGrpc.KeyManagerPixRemoveImplBase()
{
    override fun apagaChavePix(
        request: KeyRemoveRequest,
        responseObserver: StreamObserver<KeyRemoveReply>
    ) {
        val chavePix = chavePixRepository.deleteById(request.pixId.toLong())
        val response =  KeyRemoveReply.newBuilder().setClienteId(request.clienteId).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
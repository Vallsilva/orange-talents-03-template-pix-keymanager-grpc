package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.KeyManagerPixReply
import br.com.zupacademy.valeria.KeyManagerPixRequest
import br.com.zupacademy.valeria.KeyManagerPixServiceGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClienteController (@Inject val clienteRepository: ClienteRepository,
                         @Inject val clienteConsulta: ConsultaClient) : KeyManagerPixServiceGrpc.KeyManagerPixServiceImplBase(){

    override fun cadastrarChavePix(
        request: KeyManagerPixRequest,
        responseObserver: StreamObserver<KeyManagerPixReply>
    ) {
        //Fazer uma comunicação com o erp itau para preencher o objeto cliente

        val clienteResponse = clienteConsulta.consulta(request.clienteId, request.tipo.name)
        //val cliente = Cliente(clienteResponse.titular.cpf, clienteResponse.titular.id, clienteResponse.tipo)

        val chavePix = ChavePix(
            TipoChave.valueOf(request.tipoChave.toString()),
            request.valChave,
            clienteResponse.titular.cpf,
            clienteResponse.titular.id,
            clienteResponse.tipo
        )

        clienteRepository.save(chavePix)

        val response = KeyManagerPixReply.newBuilder().setValChave(request.valChave).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

}
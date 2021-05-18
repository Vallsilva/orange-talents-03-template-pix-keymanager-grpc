package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.KeyManagerPixReply
import br.com.zupacademy.valeria.KeyManagerPixRequest
import br.com.zupacademy.valeria.KeyManagerPixServiceGrpc
import br.com.zupacademy.valeria.handle.ErrorHandler
import br.com.zupacademy.valeria.handle.exception.ChavePixExistenteException
import br.com.zupacademy.valeria.handle.exception.ChavePixMaiorQueOPermitidoException
import br.com.zupacademy.valeria.handle.handles.ChavePixExistenteExceptionHandler
import com.google.rpc.StatusProto
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jdk.net.SocketFlow
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class ClienteController (@Inject val clienteRepository: ClienteRepository,
                         @Inject val clienteConsulta: ConsultaClient) : KeyManagerPixServiceGrpc.KeyManagerPixServiceImplBase(){

    override fun cadastrarChavePix(
        request: KeyManagerPixRequest,
        responseObserver: StreamObserver<KeyManagerPixReply>
    ) {
        val clienteResponse = clienteConsulta.consulta(request.clienteId, request.tipo.name)

        val chavePix = ChavePix(
            tipoChave = TipoChave.valueOf(request.tipoChave.toString()),
            valChave = request.valChave,
            cpf = clienteResponse.titular.cpf,
            clienteId = clienteResponse.titular.id,
            tipo = clienteResponse.tipo
        )

        if (chavePix.valChave.length > 77){
            throw ChavePixMaiorQueOPermitidoException("A chave informada tem um numero maior de caracteres que o esperado")
            return
        }

        if (clienteRepository.findByValChave(request.valChave).isPresent){

            throw ChavePixExistenteException("Chave Pix ja cadastrada caralho!")
            return
        }

        clienteRepository.save(chavePix)

        val response = KeyManagerPixReply.newBuilder().setIdPix(chavePix.id.toString()).setValChave(chavePix.valChave.toString()).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

}
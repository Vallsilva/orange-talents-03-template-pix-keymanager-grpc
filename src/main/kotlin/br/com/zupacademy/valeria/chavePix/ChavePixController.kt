package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.*
import br.com.zupacademy.valeria.chavePix.consultaExterna.CadastraChavePixBCB
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
    @Inject val validator: Validator,
    @Inject val clientBCB: CadastraChavePixBCB
) : KeyManagerPixServiceGrpc.KeyManagerPixServiceImplBase(){


    override fun cadastrarChavePix(
        request: KeyManagerPixRequest,
        responseObserver: StreamObserver<KeyManagerPixReply>
    ) {
        val convertedKeyType = request.getConvertedKeyType()

        if(!convertedKeyType.isValid(request.valChave)) {
            throw ChavePixInvalidaException("O valor da chave não é compativel com o esperado")
        }

        if (request.clienteId.isNullOrBlank()){
            throw IllegalStateException("Deu muito ruim!")
        }
        val clienteResponse = clienteConsulta.consulta(request.clienteId, request.tipo.name)

        if (clienteResponse == null){
            throw IllegalStateException("Deu mais ruim ainda!")
        }

        val chavePix = ChavePix(
            tipoChave = TipoChave.valueOf(request.tipoChave.toString()),
            valChave = GerarChavePix.gera(TipoChave.valueOf(request.tipoChave.toString()), request.valChave),
            cpf = clienteResponse.titular.cpf,
            clienteId = clienteResponse.titular.id,
            tipo = clienteResponse.tipo
        )

        if (chavePixRepository.findByValChave(request.valChave).isPresent){

            throw ChavePixExistenteException("Chave Pix ja cadastrada caralho!")
            return
        }
        chavePixRepository.save(chavePix)

       // val chavePixBCBResponse = clientBCB.cadastra(ChavePixBBCRequest(chavePix.tipoChave, chavePix.valChave, BankAccount(clienteResponse), Owner(clienteResponse)))

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
        chavePixRepository.deleteById(request.pixId.toLong())
        val response =  KeyRemoveReply.newBuilder().setClienteId(request.clienteId).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
        return
    }

    private fun KeyManagerPixRequest.getConvertedKeyType() : TipoChave {
        return TipoChave.valueOf(this.tipoChave.toString())
    }
}
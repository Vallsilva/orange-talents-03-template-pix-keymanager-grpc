package br.com.zupacademy.valeria.chavePix

import br.com.zupacademy.valeria.*
import br.com.zupacademy.valeria.handle.ErrorHandler
import br.com.zupacademy.valeria.handle.exception.ChavePixExistenteException
import br.com.zupacademy.valeria.handle.exception.ChavePixNaoEncontradaException
import br.com.zupacademy.valeria.handle.exception.ChavePixOutroDonoException
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@ErrorHandler
@Singleton
class ChavePixController(
    @Inject val chavePixRepository: ChavePixRepository,
    @Inject val clienteConsulta: ConsultaErpItau,
    @Inject val validator: Validator
) : KeyManagerPixServiceGrpc.KeyManagerPixServiceImplBase(){

    @Transactional
    override fun cadastrarChavePix(
        request: KeyManagerPixRequest,
        responseObserver: StreamObserver<KeyManagerPixReply>
    ) {
        if (request.clienteId.isNullOrBlank()){
            throw IllegalStateException("Deu muito ruim!")
        }
        val clienteResponse = clienteConsulta.consulta(request.clienteId, request.tipo.name)

        if (clienteResponse == null){
            throw IllegalStateException("Deu mais ruim ainda!")
        }
        val chavePix = ChavePix(
            tipoChave = TipoChave.valueOf(request.tipoChave.toString()),
            valChave = request.valChave,
            cpf = clienteResponse.titular.cpf,
            clienteId = clienteResponse.titular.id,
            tipo = clienteResponse.tipo
        )
        val chaveValida = validator.validate(chavePix)

        if (chaveValida.isNotEmpty()){
            throw ConstraintViolationException(chaveValida)
        }

        if (chavePixRepository.findByValChave(request.valChave).isPresent){

            throw ChavePixExistenteException("Chave Pix ja cadastrada caralho!")
            return
        }
        chavePixRepository.save(chavePix)

        val response = KeyManagerPixReply.newBuilder().setIdPix(chavePix.id.toString()).setValChave(chavePix.valChave.toString()).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    @Transactional
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


}
package br.com.zupacademy.valeria.chavePixTeste

import br.com.zupacademy.valeria.KeyManagerPixRequest
import br.com.zupacademy.valeria.KeyManagerPixServiceGrpc
import br.com.zupacademy.valeria.TipoChave
import br.com.zupacademy.valeria.TipoConta
import br.com.zupacademy.valeria.chavePix.ChavePixRepository
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Singleton

@MicronautTest(transactional = false)
class DeletaChaveControllerTest(
    val grpcClient: KeyManagerPixServiceGrpc.KeyManagerPixServiceBlockingStub,
    val chavePixRepository: ChavePixRepository
) {

    @Test
    fun `deveApagarAChaveCadastrada`(){

        val response = grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.CPF)
            .setValChave("02467781054")
            .setTipo(TipoConta.CONTA_CORRENTE)
            .build())

        val chaveParaDeletar = chavePixRepository.findById(response.idPix.toLong())


    }

    @Factory
    class Clients{
        @Singleton
        fun blokckingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) : KeyManagerPixServiceGrpc.KeyManagerPixServiceBlockingStub?{
            return KeyManagerPixServiceGrpc.newBlockingStub(channel)
        }
    }
}
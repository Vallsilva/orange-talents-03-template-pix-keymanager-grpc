package br.com.zupacademy.valeria.chavePixTeste


import br.com.zupacademy.valeria.KeyManagerPixRequest
import br.com.zupacademy.valeria.KeyManagerPixServiceGrpc
import br.com.zupacademy.valeria.KeyManagerPixServiceGrpc.*
import br.com.zupacademy.valeria.KeyRemoveRequest
import br.com.zupacademy.valeria.TipoChave
import br.com.zupacademy.valeria.TipoConta.*
import br.com.zupacademy.valeria.chavePix.ChavePix
import br.com.zupacademy.valeria.chavePix.ChavePixRepository
import br.com.zupacademy.valeria.chavePix.ConsultaErpItau
import br.com.zupacademy.valeria.chavePix.TipoChave.CPF
import com.google.rpc.Code
import com.google.rpc.Code.INVALID_ARGUMENT_VALUE
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import javax.inject.Singleton


@MicronautTest(transactional = false)
class CadastraChaveControllerTest(
    val grpcClient: KeyManagerPixServiceBlockingStub,
    val clienteRepository: ChavePixRepository,
    val client: ConsultaErpItau
) {

    @Test
    fun `deve Salvar Chave Pix Cpf` () {

        clienteRepository.deleteAll()

        val response = grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.CPF)
            .setValChave("02467781054")
            .setTipo(CONTA_CORRENTE)
            .build())

        with(response){
            assertNotNull(idPix)
            assertTrue(clienteRepository.existsById(idPix.toLong()))
        }
    }

    @Test
    fun `naoDeveSalvarChaveJaExistente`(){

        clienteRepository.save(ChavePix(CPF, "02467781054", "02467781054", "c56dfef4-7901-44fb-84e2-a2cefb157890", CONTA_CORRENTE.toString()))

        val erro = assertThrows<StatusRuntimeException> {
            grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.CPF)
            .setValChave("02467781054")
            .setTipo(CONTA_CORRENTE)
            .build())}

        with(erro){
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave Pix ja cadastrada caralho!", status.description)
        }

    }

    @Test
    fun `deveSalvarChavePixEmail`(){

        clienteRepository.deleteAll()

        val response = grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.EMAIL)
            .setValChave("rafael@mail.com")
            .setTipo(CONTA_CORRENTE)
            .build())

        with(response){
            assertNotNull(idPix)
            assertTrue(clienteRepository.existsById(idPix.toLong()))
        }
    }

    @Test
    fun `deveSalvarChavePixCelular`(){

        clienteRepository.deleteAll()

        val response = grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.CELULAR)
            .setValChave("+5531985874523")
            .setTipo(CONTA_CORRENTE)
            .build())

        with(response){
            assertNotNull(idPix)
            assertTrue(clienteRepository.existsById(idPix.toLong()))
        }
    }

    @Test
    fun `deveSalvarChavePixRandom`(){
        clienteRepository.deleteAll()

        //Criar o que o sistema cria
        val response = grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoChave(TipoChave.RANDOM)
            .setValChave("De1IA)5TFTDEG;J*mn_N;2nf}:7@ggiC8Tuls5gI!TKgbnw]C?Tabb6sP04=fxvxeuDU?]*H2T=sx")
            .setTipo(CONTA_CORRENTE)
            .build())

        with(response){
            assertNotNull(idPix)
            assertTrue(clienteRepository.existsById(idPix.toLong()))
        }
    }

    @Test
    fun `naoDeveSalvarChavePixRandomMaiorQue77Chars`(){

        clienteRepository.deleteAll()

        val erro = assertThrows<StatusRuntimeException>{
            grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
                .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setTipoChave(TipoChave.RANDOM)
                .setValChave("9731998487128108483796138803793119776791637435279037591573464333438322513653745")
                .setTipo(CONTA_CORRENTE)
                .build())
        }

        with(erro){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Formato de chave Pix inválido!", status.description)
        }
    }

    @Test
    fun `deveSeComunicarComERPItau`(){
        val clienteResponse = client.consulta("c56dfef4-7901-44fb-84e2-a2cefb157890", "CONTA_CORRENTE")

        with(clienteResponse){
            assertEquals(clienteResponse!!.titular.cpf, "02467781054")
        }
    }

    @Test
    fun `naoDeveSalvarChavePixCpfInvalido`(){
        clienteRepository.deleteAll()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
                .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setTipoChave(TipoChave.CPF)
                .setValChave("042677810544")
                .setTipo(CONTA_CORRENTE)
                .build())
        }

        with(response){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code )
            assertEquals("Formato de chave Pix inválido!", status.description)

        }
    }

    @Test
    fun `naoDeveSalvarChavePixComCampoNullOuVazio`(){
        clienteRepository.deleteAll()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
                .setClienteId("kaidjahjfha")
                .setTipoChave(TipoChave.CPF)
                .setValChave("14523698745")
                .setTipo(CONTA_CORRENTE)
                .build())
        }

        with(response){
            assertEquals(Status.INTERNAL.code, status.code )
            assertEquals("Problema na comunicacao", status.description)
        }
    }

    @Test
    fun `nao deve salvar uma chave com tipo cpf e a chave em formato de email`(){

        clienteRepository.deleteAll()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
                .setClienteId("5260263c-a3c1-4727-ae32-3bdb2538841b")
                .setTipoChave(TipoChave.CPF)
                .setValChave("valeria@mail.com")
                .setTipo(CONTA_CORRENTE)
                .build())
        }

        with(response){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code )
            assertEquals("Formato de chave Pix inválido!", status.description)
        }
    }

//    @Test
//    fun `nao deve excluir uma chave pix pertencente a outro clienteId`(){
//        val response = grpcClient.cadastrarChavePix(KeyManagerPixRequest.newBuilder()
//            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
//            .setTipoChave(TipoChave.CELULAR)
//            .setValChave("+5531985874523")
//            .setTipo(CONTA_CORRENTE)
//            .build())
//
//        assertThrows<StatusRuntimeException> {grpcClient.apagaChavePix(KeyRemoveRequest.newBuilder(clienteRepository.findByIdAndClienteId(response.idPix.toLong(), "5260263c-a3c1-4727-ae32-3bdb2538841b")).build()}
//        }
//        assertEquals(Status.FAILED_PRECONDITION.code, )
//        assertEquals("A chave pix não pode ser excluida por outro usuário que não seja seu dono!", )
//
//
//    }

    @Factory
    class Clients{
        @Singleton
        fun blokckingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) : KeyManagerPixServiceBlockingStub?{
            return KeyManagerPixServiceGrpc.newBlockingStub(channel)
        }
    }
}
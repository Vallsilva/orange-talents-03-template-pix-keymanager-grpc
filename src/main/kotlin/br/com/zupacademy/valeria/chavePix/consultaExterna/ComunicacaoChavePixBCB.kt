package br.com.zupacademy.valeria.chavePix.consultaExterna

import br.com.zupacademy.valeria.chavePix.ChavePixBBCRequest
import br.com.zupacademy.valeria.chavePix.ChavePixBCBResponse
import br.com.zupacademy.valeria.chavePix.DeletePixKeyRequest
import br.com.zupacademy.valeria.chavePix.DeletePixKeyResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082/api/v1")
interface ComunicacaoChavePixBCB {

    @Post("/pix/keys")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun cadastra(@Body createPixKeyRequest: ChavePixBBCRequest) : ChavePixBCBResponse

    @Get("/pix/keys/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun consulta(@PathVariable id: String) : ChavePixBCBResponse?

    @Delete("/pix/keys/{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun deleta(@PathVariable id: String, @Body deletePixKeyRequest: DeletePixKeyRequest) : DeletePixKeyResponse
}
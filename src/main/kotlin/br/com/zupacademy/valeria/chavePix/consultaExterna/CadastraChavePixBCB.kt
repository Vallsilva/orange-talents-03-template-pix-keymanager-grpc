package br.com.zupacademy.valeria.chavePix.consultaExterna

import br.com.zupacademy.valeria.chavePix.ChavePixBBCRequest
import br.com.zupacademy.valeria.chavePix.ChavePixBCBResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082/api/v1/pix/keys")
interface CadastraChavePixBCB {

    @Post
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun cadastra(@Body createPixKeyRequest: ChavePixBBCRequest) : ChavePixBCBResponse
}
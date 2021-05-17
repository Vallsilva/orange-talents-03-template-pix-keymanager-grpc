package br.com.zupacademy.valeria.chavePix

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091/api/v1/clientes")
interface ConsultaClient {

    @Get("/{id}/contas")
    fun consulta(@PathVariable id: String?, @QueryValue tipo: String) : ClienteResponse
}
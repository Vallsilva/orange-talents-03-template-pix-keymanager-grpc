package br.com.zupacademy.valeria.handle.handles

import br.com.zupacademy.valeria.handle.ExceptionHandler
import br.com.zupacademy.valeria.handle.ExceptionHandler.*
import io.grpc.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import javax.inject.Singleton

@Singleton
class HttpClientResponseExceptionHandler : ExceptionHandler<HttpClientResponseException> {

    override fun handle(e: HttpClientResponseException): StatusWithDetails {
        return StatusWithDetails(
            Status.INTERNAL
                .withDescription("Problema na comunicacao")
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is HttpClientResponseException
    }
}
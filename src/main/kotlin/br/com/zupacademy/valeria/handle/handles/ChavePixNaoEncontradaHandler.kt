package br.com.zupacademy.valeria.handle.handles

import br.com.zupacademy.valeria.handle.ExceptionHandler
import br.com.zupacademy.valeria.handle.ExceptionHandler.*
import br.com.zupacademy.valeria.handle.exception.ChavePixNaoEncontradaException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ChavePixNaoEncontradaHandler : ExceptionHandler<ChavePixNaoEncontradaException> {

    override fun handle(e: ChavePixNaoEncontradaException): StatusWithDetails {
        return StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ChavePixNaoEncontradaException
    }


}
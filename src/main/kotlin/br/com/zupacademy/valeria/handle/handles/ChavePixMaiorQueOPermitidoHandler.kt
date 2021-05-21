package br.com.zupacademy.valeria.handle.handles

import br.com.zupacademy.valeria.handle.ExceptionHandler
import br.com.zupacademy.valeria.handle.ExceptionHandler.*
import br.com.zupacademy.valeria.handle.exception.ChavePixInvalidaException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ChavePixMaiorQueOPermitidoHandler : ExceptionHandler<ChavePixInvalidaException> {

    override fun handle(e: ChavePixInvalidaException): StatusWithDetails {
        return StatusWithDetails(
            Status.INVALID_ARGUMENT
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ChavePixInvalidaException
    }
}
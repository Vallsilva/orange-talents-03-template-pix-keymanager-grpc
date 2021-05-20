package br.com.zupacademy.valeria.handle.handles

import br.com.zupacademy.valeria.handle.ExceptionHandler
import br.com.zupacademy.valeria.handle.ExceptionHandler.*
import br.com.zupacademy.valeria.handle.exception.ChavePixOutroDonoException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ChavePixOutroDonoExceptionHandler : ExceptionHandler<ChavePixOutroDonoException> {

        override fun handle(e: ChavePixOutroDonoException): StatusWithDetails {
            return StatusWithDetails(
                Status.FAILED_PRECONDITION
                    .withDescription(e.message)
                    .withCause(e)
            )
        }

        override fun supports(e: Exception): Boolean {
            return e is ChavePixOutroDonoException
        }
}
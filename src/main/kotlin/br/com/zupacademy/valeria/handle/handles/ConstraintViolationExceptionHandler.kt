package br.com.zupacademy.valeria.handle.handles

import br.com.zupacademy.valeria.handle.ExceptionHandler
import br.com.zupacademy.valeria.handle.ExceptionHandler.StatusWithDetails
import com.google.protobuf.Any
import com.google.rpc.BadRequest
import com.google.rpc.Code.INVALID_ARGUMENT_VALUE
import javax.inject.Singleton
import javax.validation.ConstraintViolationException


@Singleton
class ConstraintViolationExceptionHandler  : ExceptionHandler<ConstraintViolationException> {

    override fun handle(e: ConstraintViolationException): StatusWithDetails {

        val details = BadRequest.newBuilder()
            .addAllFieldViolations(e.constraintViolations.map {
                BadRequest.FieldViolation.newBuilder()
                    .setField(it.propertyPath.last().name ?: "?? key ??") // TODO: handle class-level constraint
                    .setDescription(it.message)
                    .build()
            })
            .build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(INVALID_ARGUMENT_VALUE)
            .setMessage("Formato de chave Pix inválido!")
            .addDetails(Any.pack(details))
            .build()

        return StatusWithDetails(statusProto)
    }

    override fun supports(e: Exception): Boolean {
        return e is ConstraintViolationException
    }
}
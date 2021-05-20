package br.com.zupacademy.valeria.chavePix

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ChavePixValidador::class])
annotation class ValidadorChavePix(
    val message: String = "Formato de chave Pix inválido!",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

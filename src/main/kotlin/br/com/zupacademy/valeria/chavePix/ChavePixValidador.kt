package br.com.zupacademy.valeria.chavePix

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton

@Singleton
class ChavePixValidador : ConstraintValidator<ValidadorChavePix, ChavePix> {

    override fun isValid(
        value: ChavePix?,
        annotationMetadata: AnnotationValue<ValidadorChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {

        if (value?.tipoChave == null){
            return false
        }

        return value.tipoChave.isValid(value = value.valChave)
    }
}
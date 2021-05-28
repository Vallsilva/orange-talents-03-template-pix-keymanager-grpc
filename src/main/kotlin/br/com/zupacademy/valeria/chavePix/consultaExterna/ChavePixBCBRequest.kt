package br.com.zupacademy.valeria.chavePix

import java.time.LocalDateTime

data class ChavePixBBCRequest(
    val keyType: TipoChave,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
) {

}

data class ChavePixBCBResponse(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: String
) {
}

data class BankAccount(
    val participant: String,
    val branch: String, //agencia
    val accountNumber: String,
    val accountType: String
){
    constructor(clienteResponse: ClienteResponse) : this(
        clienteResponse.instituicao.ispb,
        clienteResponse.agencia,
        clienteResponse.numero,
        "CACC"
    )
}

data class Owner(
    val type: String,
    val name: String,
    val taxIdNumber: String
){
    constructor(clienteResponse: ClienteResponse) : this(
        "NATURAL_PERSON",
        clienteResponse.titular.nome,
        clienteResponse.titular.cpf
    )
}

data class DeletePixKeyRequest(
    val key: String,
    val participant: String
) {
}

data class DeletePixKeyResponse(
    val key: String,
    val participant: String,
    val deletedAt: LocalDateTime
){

}
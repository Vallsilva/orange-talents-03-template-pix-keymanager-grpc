package br.com.zupacademy.valeria.chavePix

data class ClienteResponse (val tipo: String,
                            val instituicao: InstituicaoResponse,
                            val agencia: String,
                            val numero: String,
                            val titular: TitluarResponse){
}

data class TitluarResponse(val id: String,
                           val nome: String,
                           val cpf: String){

}

data class InstituicaoResponse(val nome: String, val ispb: String){

}
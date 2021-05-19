package br.com.zupacademy.valeria.chavePix

enum class TipoChave{

    CPF{
        override fun isValid(value: String?): Boolean{
            value ?: return false
            if (value.length == 11){
                return value.matches(Regex("^[0-9]{11}\$"))}
            else return false
        }
    },
    CELULAR{
        override fun isValid(value: String?): Boolean{
            value ?: return false
            if (value.length == 14){
                return value.matches(Regex("^\\+[1-9][0-9]\\d{1,14}\$"))}
            else return false
        }
    },
    EMAIL{
        override fun isValid(value: String?): Boolean{
            value ?: return false
            return value.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))
        }
    },
    RANDOM{
        override fun isValid(value: String?): Boolean{
            return value.isNullOrBlank()
        }
    };

    abstract fun isValid(value: String?): Boolean
}
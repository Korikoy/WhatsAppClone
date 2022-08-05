package com.example.whatsapp.model
import java.io.Serializable

class Mensagem : Serializable{

    var idUser: String = ""
    var mensagem: String? = null
    var foto: String? = null
}
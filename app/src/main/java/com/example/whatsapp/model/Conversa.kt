package com.example.whatsapp.model

import com.example.whatsapp.config.ConfiguracaoFirebase
import com.google.firebase.database.DatabaseReference
import java.io.Serializable

class Conversa: Serializable {
    var remetente: String? = null
    var destinat: String? = null
    var ultimaMsg: String? = null
    var usuario: Usuario? = null



    fun salvar(){
        val database: DatabaseReference = ConfiguracaoFirebase.getDataRef()
        val conversaRef = database.child("conversas")
        conversaRef.child(this.remetente!!).child(this.destinat!!).setValue(this)
    }
}

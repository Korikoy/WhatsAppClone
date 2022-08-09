package com.example.whatsapp.model

import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.helper.UserFirebase
import com.google.firebase.database.Exclude
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable


class Usuario(
    var nome:String = "",
    var email: String = "",
    var foto:String? = null,
    @get: Exclude var senha:String = "",
    @get: Exclude var id:String = ""
): Serializable{
    fun salvar(){
         val dataBase = ConfiguracaoFirebase.getDataRef()
        dataBase.child("usuarios").child(this.id).setValue(this)

    }
    fun atualizar(){
        val id = UserFirebase.IdUser()
        val database = ConfiguracaoFirebase.getDataRef()
        val usuarioRef = database.child("usuarios").child(id)

        val valorUser = map()
        usuarioRef.updateChildren(valorUser)
    }

    @Exclude
    fun map(): Map<String,Any>{
        val usermap: HashMap<String,Any> = HashMap()
        usermap.put("email", email)
        usermap.put("nome", nome)
        usermap.put("foto", foto!!)

        return usermap
    }



}
package com.example.whatsapp.helper

import android.net.Uri
import android.util.Log
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserFirebase {
    companion object{
        fun IdUser(): String {

            val usuario: FirebaseAuth = ConfiguracaoFirebase.getAuth()
            val email: String = usuario.currentUser?.email.toString()

            return Base64Custom.codificarBase64(email)
        }

        fun getUsuarioAtual(): FirebaseUser{
            val usuario: FirebaseAuth = ConfiguracaoFirebase.getAuth()
            return usuario.currentUser!!


        }
        fun atualizarFotoUsuario(url: Uri): Boolean {
            try {


                val user = getUsuarioAtual()
                val profile = UserProfileChangeRequest.Builder().setPhotoUri(url).build()
                user.updateProfile(profile).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.")
                    } else {

                    }

                }
                return true
            }catch (e:Exception){
                e.printStackTrace()
                return false
            }
        }
        fun atualizarNomeUsuario(nome: String): Boolean {
            try {


                val user = getUsuarioAtual()
                val profile = UserProfileChangeRequest.Builder().setDisplayName(nome).build()
                user.updateProfile(profile).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.")
                    } else {

                    }

                }
                return true
            }catch (e:Exception){
                e.printStackTrace()
                return false
            }
        }
        fun usuarioLogadoData(): Usuario {
            val firebaseUser = getUsuarioAtual()
            val dataUser = Usuario()
            dataUser.email = firebaseUser.email.toString()
            dataUser.nome = firebaseUser.displayName.toString()
            if(firebaseUser.photoUrl == null){
                dataUser.foto = ""
            }else{
                dataUser.foto = firebaseUser.photoUrl.toString()
            }

            return dataUser

        }
    }


}
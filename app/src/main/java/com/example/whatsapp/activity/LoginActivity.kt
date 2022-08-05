package com.example.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.databinding.ActivityLoginBinding
import com.example.whatsapp.model.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var auth = ConfiguracaoFirebase.getAuth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.TextCadastro.setOnClickListener {
            abrirTelaCadastro()
        }
        binding.buttonLogar.setOnClickListener {
            validarUsuario()
        }


    }

    fun validarUsuario(){
        if(!binding.loginEmail.text.toString().isEmpty()){
            if (!binding.loginSenha.text.toString().isEmpty()){
                val usuario = Usuario()
                usuario.email = binding.loginEmail.text.toString()
                usuario.senha = binding.loginSenha.text.toString()
                logarUsuario(usuario)

            }else{
                Toast.makeText(this,"Preencha a senha!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Preencha o email!", Toast.LENGTH_LONG).show()
        }
    }




    fun abrirTelaCadastro(){
        val intent: Intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
    }
    fun logarUsuario(usuario: Usuario){
        auth.signInWithEmailAndPassword(
            usuario.email,usuario.senha
        ).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                abrirTelaPrincial()
            }else{
                var exc = ""
                try {
                    throw task.exception!!
                }catch (e: FirebaseAuthInvalidUserException){
                    exc = "Usuario nao esta cadastrado"
                }catch (e: FirebaseAuthInvalidCredentialsException){
                    exc = "E-mail e senha nao correspondem a um usuario"
                }catch (e: Exception){
                    exc = "Erro ao cadastrar usuario: " + e.message
                    e.printStackTrace()
                }
                Toast.makeText(this,exc,Toast.LENGTH_LONG).show()
            }
        })
    }
    fun abrirTelaPrincial(){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        val usuarioAtual : FirebaseUser? = auth.currentUser
        if(usuarioAtual != null){
            abrirTelaPrincial()
        }
    }
}
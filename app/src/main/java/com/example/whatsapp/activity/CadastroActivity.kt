package com.example.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.databinding.ActivityCadastroBinding
import com.example.whatsapp.helper.Base64Custom
import com.example.whatsapp.helper.UserFirebase
import com.example.whatsapp.model.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*


class CadastroActivity : AppCompatActivity() {
    lateinit var binding: ActivityCadastroBinding
    private var auth = ConfiguracaoFirebase.getAuth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCadastrar.setOnClickListener {
            validarCadastroUsuario()
        }

    }



    fun validarCadastroUsuario(){
        val textoNome = binding.editNome.text.toString()
        val textoEmail = binding.editEmail.text.toString()
        val textoSenha = binding.editSenha.text.toString()
        if(!textoNome.isEmpty()){
            if (!textoEmail.isEmpty()){
                if(!textoSenha.isEmpty()){
                    var usuario = Usuario()
                    usuario.nome = textoNome
                    usuario.email = textoEmail
                    usuario.senha = textoSenha
                    cadastrarUsuario(usuario)
                }else{
                    Toast.makeText(this,"Preencha a senha!",Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(this,"Preencha o email!",Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this,"Preencha o nome!",Toast.LENGTH_LONG).show()
        }


    }
    fun cadastrarUsuario(usuario: Usuario){
        auth.createUserWithEmailAndPassword(
            usuario.email, usuario.senha
        ).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if (task.isSuccessful) {
                val identificadorUsuario = Base64Custom.codificarBase64(usuario.email)
                usuario.id = identificadorUsuario
                usuario.salvar()
                Toast.makeText(this, "Sucesso ao cadastrar usuario", Toast.LENGTH_LONG).show()
                UserFirebase.atualizarNomeUsuario(usuario.nome)
                finish()

            } else {
                var excecao = ""
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    excecao = "Digite uma senha mais forte!"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    excecao = "Digite um email valido!"
                } catch (e: FirebaseAuthUserCollisionException) {
                    excecao = "Essa conta ja foi cadastrada"
                } catch (e: Exception) {
                    excecao = "Erro ao cadastrar usuario!" + e.message
                    e.printStackTrace()
                }
                Toast.makeText(this, excecao, Toast.LENGTH_LONG).show()
            }
        })
    }
}
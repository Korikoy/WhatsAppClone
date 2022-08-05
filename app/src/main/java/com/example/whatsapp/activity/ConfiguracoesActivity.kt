package com.example.whatsapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.whatsapp.R
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.databinding.ActivityConfiguracoesBinding
import com.example.whatsapp.helper.Base64Custom
import com.example.whatsapp.helper.Permissao
import com.example.whatsapp.helper.UserFirebase
import com.example.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream


class ConfiguracoesActivity : AppCompatActivity() {
    private var idUser = UserFirebase.IdUser()
    private var storageRef: StorageReference = FirebaseStorage.getInstance().getReference()
    private var permission = arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA)
    private val usuarioLogado = UserFirebase.usuarioLogadoData()
    lateinit var binding: ActivityConfiguracoesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Permissao.validarPermissoes(permission, this,1)

        val toolbar = binding.include.toolbarPrincipal
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val usuario = UserFirebase.getUsuarioAtual()
        val url : Uri? = usuario.photoUrl
        if (url != null){
            Glide.with(this.applicationContext).load(url).into(binding.circleImageViewFotoPerfil)
        }else{
            binding.circleImageViewFotoPerfil.setImageResource(R.drawable.padrao)
        }
        binding.editNomeConf.setText(usuario.displayName)

        binding.imageButtonCamera.setOnClickListener { it ->
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            validatePicture.launch(i)

        }
        binding.imageButtonGalery.setOnClickListener {
            val i= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            validateGallery.launch(i)
        }
        binding.imageName.setOnClickListener {
            val nome: String = binding.editNomeConf.text.toString()
            val retorno: Boolean = UserFirebase.atualizarNomeUsuario(nome)
            usuarioLogado.nome = nome
            usuarioLogado.atualizar()
            if(retorno){
                Toast.makeText(applicationContext, "Nome atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(premResult: Int in grantResults){
            if(premResult == PackageManager.PERMISSION_DENIED){
                alertaValidPerms()

            }

        }
    }
    fun alertaValidPerms(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissoes Negadas")
        builder.setMessage("Para ultilizar o app e necessario aceitar as permssoes!")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar") { p0, p1 -> finish() }
        val dialog = builder.create()
        dialog.show()

    }
    private val validatePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val bitmap = it?.data?.extras?.get("data") as Bitmap
        binding.circleImageViewFotoPerfil.setImageBitmap(bitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
        val data: ByteArray = byteArrayOutputStream.toByteArray()

        val imageRef = storageRef.child("imagens").child("perfil").child("$idUser.jpeg")
        val uploadTask: UploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext, "Falha no upload", Toast.LENGTH_SHORT).show()
        }
        uploadTask.addOnSuccessListener {
            Toast.makeText(applicationContext, "Sucesso no Upload", Toast.LENGTH_SHORT).show()
            imageRef.downloadUrl.addOnCompleteListener { task->
                val url: Uri = task.result
                atualizaFotoUsuario(url)
            }

        }
    }


    private val validateGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val image = it?.data?.data
        binding.circleImageViewFotoPerfil.setImageURI(image)
        val baos = ByteArrayOutputStream()
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val dados: ByteArray = baos.toByteArray()
        val imgRef = storageRef.child("imagens").child("perfil").child("$idUser.jpeg")
        val uploadTask: UploadTask = imgRef.putBytes(dados)
        uploadTask.addOnFailureListener {
            Toast.makeText(applicationContext, "Falha no upload", Toast.LENGTH_SHORT).show()
        }
        uploadTask.addOnCompleteListener{
            Toast.makeText(applicationContext, "Sucesso no Upload", Toast.LENGTH_SHORT).show()
            imgRef.downloadUrl.addOnCompleteListener { task->
                val url: Uri = task.result
                atualizaFotoUsuario(url)
            }
    }
    }
    fun atualizaFotoUsuario(url: Uri){
       val retorno:Boolean = UserFirebase.atualizarFotoUsuario(url)
        if(retorno){
            usuarioLogado.foto = url.toString()
            usuarioLogado.atualizar()
            Toast.makeText(applicationContext, "Sua foto foi alterada", Toast.LENGTH_SHORT).show()
        }

    }


}
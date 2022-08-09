package com.example.whatsapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.whatsapp.R
import com.example.whatsapp.adapter.ContatosAdapter
import com.example.whatsapp.adapter.MensagensAdapter
import com.example.whatsapp.config.ConfiguracaoFirebase

import com.example.whatsapp.databinding.ActivityChatBinding
import com.example.whatsapp.helper.Base64Custom
import com.example.whatsapp.helper.UserFirebase
import com.example.whatsapp.model.Conversa
import com.example.whatsapp.model.Mensagem
import com.example.whatsapp.model.Usuario
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var userDest : Usuario
    private var idUsuarioRem = UserFirebase.IdUser()
    private lateinit var idUsuarioDest: String
    lateinit var adapter: MensagensAdapter
    var lista = ArrayList<Mensagem>()
    private val database: DatabaseReference = ConfiguracaoFirebase.getDataRef()
    private lateinit var mensagens : DatabaseReference
    private lateinit var childEventListener : ChildEventListener
    private var storage : StorageReference = ConfiguracaoFirebase.getStorageRef()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        if (bundle != null){
            userDest = bundle.getSerializable("chat") as Usuario
            binding.textViewNomeChat.text = userDest.nome
            val foto = userDest.foto
            if(foto != null){
                val url: Uri = Uri.parse(userDest.foto)
                Glide.with(this).load(url).into(binding.circleImageFotoChat)

            }else{
                binding.circleImageFotoChat.setImageResource(R.drawable.padrao)
            }
            idUsuarioDest = Base64Custom.codificarBase64(userDest.email!!)
        }
        adapter = MensagensAdapter(lista,this)



        val layoutManeger = LinearLayoutManager(this)
        binding.contentChat.recycleMensagens.layoutManager = layoutManeger
        binding.contentChat.recycleMensagens.setHasFixedSize(true)
        binding.contentChat.recycleMensagens.adapter = adapter

        binding.contentChat.fabEnviar1.setOnClickListener{enviarMensagem()}
        binding.contentChat.enviarFoto.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            validatePicture.launch(i)
        }
        binding.contentChat.galery.setOnClickListener {
            val i= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            validateGallery.launch(i)
        }

    }
    fun enviarMensagem(){
        val mensagem: String = binding.contentChat.textEnviar.text.toString()
        if(!mensagem.isEmpty()){
            val msg = Mensagem()
            msg.idUser = idUsuarioRem
            msg.mensagem = mensagem

            salvarMensagem(idUsuarioRem,idUsuarioDest, msg)
            salvarMensagem(idUsuarioDest,idUsuarioRem, msg)
            salvarConversa(msg)

        }else{
            Toast.makeText(this,"Digite uma mensagem para enviar!",Toast.LENGTH_LONG).show()
        }
    }

    fun salvarMensagem(idRem:String,idDest:String,msg:Mensagem){
        val database: DatabaseReference = ConfiguracaoFirebase.getDataRef()
        val msgRef = database.child("mensagens")
        msgRef.child(idRem).child(idDest).push().setValue(msg)
        binding.contentChat.textEnviar.setText("")
    }

    override fun onStart() {
        super.onStart()
        recuperarMsg()
    }

    override fun onStop() {
        super.onStop()
        mensagens.removeEventListener(childEventListener)
        lista.clear()
    }
    fun recuperarMsg(){
        mensagens = database.child("mensagens").child(idUsuarioRem).child(idUsuarioDest)
        childEventListener = mensagens.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mensagem = snapshot.getValue(Mensagem::class.java)
                lista.add(mensagem!!)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
        val data: ByteArray = byteArrayOutputStream.toByteArray()

        val nomeImagem = UUID.randomUUID().toString()

        val imageRef = storage.child("imagens").child("fotos").child(idUsuarioRem).child(nomeImagem)

        val uploadTask: UploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext, "Falha no upload", Toast.LENGTH_SHORT).show()
        }
        uploadTask.addOnSuccessListener {
            Toast.makeText(applicationContext, "Sucesso no Upload", Toast.LENGTH_SHORT).show()
            imageRef.downloadUrl.addOnCompleteListener { task->
                val url: String = task.result.toString()
                val mensagem = Mensagem()
                mensagem.idUser = idUsuarioRem
                mensagem.foto = url

                salvarMensagem(idUsuarioRem,idUsuarioDest,mensagem)
                salvarMensagem(idUsuarioDest,idUsuarioRem,mensagem)



            }

    }
}

    private val validateGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val image = it?.data?.data
        val baos = ByteArrayOutputStream()
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val dados: ByteArray = baos.toByteArray()
        val nomeImagem = UUID.randomUUID().toString()
        val imageRef = storage.child("imagens").child("fotos").child(idUsuarioRem).child(nomeImagem)
        val uploadTask: UploadTask = imageRef.putBytes(dados)
        uploadTask.addOnFailureListener {
            Toast.makeText(applicationContext, "Falha no upload", Toast.LENGTH_SHORT).show()
        }
        uploadTask.addOnCompleteListener{
            Toast.makeText(applicationContext, "Sucesso no Upload", Toast.LENGTH_SHORT).show()
            imageRef.downloadUrl.addOnCompleteListener { task->
                val url: String = task.result.toString()
                val mensagem = Mensagem()
                mensagem.idUser = idUsuarioRem
                mensagem.foto = url

                salvarMensagem(idUsuarioRem,idUsuarioDest,mensagem)
                salvarMensagem(idUsuarioDest,idUsuarioRem,mensagem)

            }
        }
    }

fun salvarConversa(msg: Mensagem){

    val conversaReme = Conversa()
    conversaReme.remetente = idUsuarioRem
    conversaReme.destinat = idUsuarioDest
    conversaReme.ultimaMsg = msg.mensagem
    conversaReme.usuario = userDest
    conversaReme.salvar()



}

}


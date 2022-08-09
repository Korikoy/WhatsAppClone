package com.example.whatsapp.activity

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp.adapter.GrupoContatosAdapter
import com.example.whatsapp.adapter.GrupoSelecionadoAdapter
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.databinding.ActivityGrupoBinding
import com.example.whatsapp.databinding.ToolbarBinding
import com.example.whatsapp.helper.UserFirebase
import com.example.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.Serializable

class GrupoActivity : AppCompatActivity(), GrupoContatosAdapter.OnItemClickListener,GrupoSelecionadoAdapter.OnItemClickListener2 {
    lateinit var recycleMembersSelected: RecyclerView
    lateinit var recycleMembers: RecyclerView
    private lateinit var contatosAdaper: GrupoContatosAdapter
    private lateinit var grupoAdaper: GrupoSelecionadoAdapter
    private var lista: ArrayList<Usuario> = ArrayList()
    private var listaEscolha: ArrayList<Usuario> = ArrayList()
    private lateinit var membroEventListener: ValueEventListener
    private var usuarioRef = ConfiguracaoFirebase.getDataRef().child("usuarios")
    var usernow: FirebaseUser = UserFirebase.getUsuarioAtual()
    private lateinit var binding: ActivityGrupoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = "Novo Grupo"
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycleMembersSelected = binding.include.recycleMembroSelect
        recycleMembers = binding.include.recycleMembros

        contatosAdaper = GrupoContatosAdapter(lista,this,this)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        val layoutManager2: RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycleMembers.layoutManager = layoutManager
        recycleMembers.setHasFixedSize(true)
        recycleMembers.adapter = contatosAdaper

        grupoAdaper = GrupoSelecionadoAdapter(listaEscolha,this,this)
        recycleMembersSelected.layoutManager = layoutManager2
        recycleMembersSelected.setHasFixedSize(true)
        recycleMembersSelected.adapter = grupoAdaper









        binding.fabAvancarCadastro.setOnClickListener { view ->
            val intent: Intent = Intent(this,CadastroGrupoActivity::class.java)
            intent.putExtra("membros",listaEscolha as Serializable)
            startActivity(intent)
        }

    }
    override fun onStart() {
        super.onStart()
        recuperarContatos()
    }

    override fun onStop() {
        super.onStop()
        usuarioRef.removeEventListener(membroEventListener)
        lista.clear()
    }

    fun recuperarContatos(){
        membroEventListener = usuarioRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dadosUser in snapshot.children){
                    val usuario = dadosUser.getValue(Usuario::class.java)
                    if (!usernow.email.equals(usuario?.email)) {
                        lista.add(usuario!!)
                    } else {
                    }
                }
                contatosAdaper.notifyDataSetChanged()
                atualizarMembrosToolbar()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onClick(position: Int) {
        val usuario:Usuario = lista.get(position)
        lista.remove(usuario)
        listaEscolha.add(usuario)
        grupoAdaper.notifyDataSetChanged()
        contatosAdaper.notifyDataSetChanged()
        atualizarMembrosToolbar()


    }

    override fun onClick2(position: Int) {
        val usuario:Usuario = listaEscolha.get(position)
        listaEscolha.remove(usuario)
        lista.add(usuario)
        grupoAdaper.notifyDataSetChanged()
        contatosAdaper.notifyDataSetChanged()
        atualizarMembrosToolbar()
    }

    fun atualizarMembrosToolbar(){
        val totSelected = listaEscolha.size
        val total = lista.size + totSelected
        binding.toolbar.subtitle = "$totSelected de $total selecionados"
    }

}

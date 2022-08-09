package com.example.whatsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.adapter.ContatosAdapter
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.databinding.FragmentContatosBinding
import com.example.whatsapp.helper.UserFirebase
import com.example.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ContatosFragment : Fragment() {
    private lateinit var binding: FragmentContatosBinding
    var lista = ArrayList<Usuario>()
    lateinit var adapter: ContatosAdapter
    private var usuarioRef = ConfiguracaoFirebase.getDataRef().child("usuarios")
    var usernow: FirebaseUser = UserFirebase.getUsuarioAtual()
    private lateinit var contatoEventListener: ValueEventListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentContatosBinding.inflate(layoutInflater)
        val view: View = binding.root
        adapter = activity?.let { ContatosAdapter(lista, it) }!!
        recycleContato()

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        recuperarContatos()
    }

    override fun onStop() {
        super.onStop()
        usuarioRef.removeEventListener(contatoEventListener)
        lista.clear()
    }


    fun recycleContato(){
        val layoutManager = LinearLayoutManager(activity)
        binding.recycleViewListaContatos.layoutManager = layoutManager
        binding.recycleViewListaContatos.setHasFixedSize(true)
        binding.recycleViewListaContatos.adapter = adapter
    }
    fun recuperarContatos(){
        var itemGrupo = Usuario()
        itemGrupo.nome = "Novo Grupo"
        itemGrupo.email = ""
        lista.add(itemGrupo)
        contatoEventListener = usuarioRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              for (dadosUser in snapshot.children){
                  val usuario = dadosUser.getValue(Usuario::class.java)
                  if (!usernow.email.equals(usuario?.email)) {
                      lista.add(usuario!!)
                  } else {
                  }
              }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}
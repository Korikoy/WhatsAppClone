package com.example.whatsapp.fragment

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.adapter.ContatosAdapter
import com.example.whatsapp.adapter.ConversasAdapter
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.databinding.FragmentConversasBinding
import com.example.whatsapp.helper.UserFirebase
import com.example.whatsapp.model.Conversa
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ConversasFragment : Fragment() {
    private lateinit var binding: FragmentConversasBinding
    var lista = ArrayList<Conversa>()
    lateinit var adapter: ConversasAdapter
    private lateinit var database: DatabaseReference
    lateinit var usernow: FirebaseUser
    lateinit var conversasRef: DatabaseReference
    private lateinit var childEventListener: ChildEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val idUser = UserFirebase.IdUser()

        database = ConfiguracaoFirebase.getDataRef()
        conversasRef = database.child("conversas").child(idUser)
        binding = FragmentConversasBinding.inflate(layoutInflater)
        val view: View = binding.root
        adapter = activity?.let { ConversasAdapter(lista, it) }!!
        recycleConversa()
        usernow = UserFirebase.getUsuarioAtual()
        return view

    }
fun recycleConversa() {
    val layoutManager = LinearLayoutManager(activity)
    binding.recycleListaConversas.layoutManager = layoutManager
    binding.recycleListaConversas.setHasFixedSize(true)
    binding.recycleListaConversas.adapter = adapter

}

    override fun onStart() {
        super.onStart()
        recuperarConversas()
    }

    override fun onStop() {
        super.onStop()
        conversasRef.removeEventListener(childEventListener)
        lista.clear()
    }

    fun pesquisarConversas(texto: String){
        val listaConversaBusca: ArrayList<Conversa> = ArrayList()
        for(conversa:Conversa in lista){
            val nome = conversa.usuario?.nome?.toLowerCase()
            val ultMsg = conversa.ultimaMsg?.toLowerCase()
            if(nome!!.contains(texto) || ultMsg!!.contains(texto)){
                listaConversaBusca.add(conversa)
            }
        }
        adapter = activity?.let { ConversasAdapter(listaConversaBusca, it) }!!
        recycleConversa()
        adapter.notifyDataSetChanged()
    }

    fun recarregarConversas(){
        adapter = activity?.let { ConversasAdapter(lista, it) }!!
        recycleConversa()
        adapter.notifyDataSetChanged()
    }




    fun recuperarConversas(){
       childEventListener = conversasRef.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val conversa = snapshot.getValue(Conversa::class.java)
                lista.add(conversa!!)
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

}
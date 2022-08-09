package com.example.whatsapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsapp.R
import com.example.whatsapp.activity.ChatActivity
import com.example.whatsapp.activity.GrupoActivity
import com.example.whatsapp.databinding.AdapterContatosBinding
import com.example.whatsapp.databinding.AdapterGrupoSelecionadoBinding
import com.example.whatsapp.model.Usuario

class GrupoSelecionadoAdapter(
    listaContato: ArrayList<Usuario>,
    var context: Context,
    private val onItemClickListener2: OnItemClickListener2
): RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder>() {
    private val contatos = listaContato

    inner class
    MyViewHolder(val binding: AdapterGrupoSelecionadoBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener{
                onItemClickListener2.onClick2(adapterPosition)
            }




        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding =
            AdapterGrupoSelecionadoBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GrupoSelecionadoAdapter.MyViewHolder, position: Int) {
        val usuario: Usuario = contatos[position]
        holder.binding.textNomeContatoGrupo.text = usuario.nome
        if(usuario.foto != null){
            val uri = Uri.parse(usuario.foto)
            Glide.with(context).load(uri).into(holder.binding.imageViewFotoContatoGrupo)
        } else{
            holder.binding.imageViewFotoContatoGrupo.setImageResource(R.drawable.padrao)
        }
    }

    override fun getItemCount(): Int {
        return contatos.size
    }
    interface OnItemClickListener2{
        fun onClick2(position: Int)
    }
}
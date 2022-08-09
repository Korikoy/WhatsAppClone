package com.example.whatsapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsapp.R
import com.example.whatsapp.activity.GrupoActivity
import com.example.whatsapp.databinding.AdapterContatosBinding
import com.example.whatsapp.model.Usuario


class GrupoContatosAdapter(
    listaContato: ArrayList<Usuario>,
    var context: Context,
    private val onItemClickListener: OnItemClickListener
    ): RecyclerView.Adapter<GrupoContatosAdapter.MyViewHolder>() {
    private val contatos = listaContato


    inner class
    MyViewHolder(val binding: AdapterContatosBinding, onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                onItemClickListener.onClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding =
            AdapterContatosBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding, onItemClickListener)
    }


    override fun onBindViewHolder(holder: GrupoContatosAdapter.MyViewHolder, position: Int) {
        val usuario: Usuario = contatos[position]
        val cabecalho: Boolean = usuario.email == ""
        holder.binding.textNomeContato.text = usuario.nome
        holder.binding.textEmailContato.text = usuario.email
        if(usuario.foto != null){
            val uri = Uri.parse(usuario.foto)
            Glide.with(context).load(uri).into(holder.binding.imageViewFotoContato)
        }else if (cabecalho)
        { holder.binding.imageViewFotoContato.setImageResource(R.drawable.icone_grupo)
            holder.binding.textEmailContato.visibility = View.GONE
        }else{
            holder.binding.imageViewFotoContato.setImageResource(R.drawable.padrao)
        }
    }

    override fun getItemCount(): Int {
        return contatos.size
    }
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    interface OnItemClickListener {
        fun onClick(position: Int)
    }





}


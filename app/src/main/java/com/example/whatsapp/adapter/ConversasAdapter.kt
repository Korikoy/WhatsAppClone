package com.example.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsapp.R
import com.example.whatsapp.databinding.AdapterContatosBinding
import com.example.whatsapp.model.Conversa
import com.example.whatsapp.model.Usuario

class ConversasAdapter(
    listaConversas: ArrayList<Conversa>,
    var context: Context
): RecyclerView.Adapter<ConversasAdapter.MyViewHolder>() {
    private val conversas = listaConversas

    inner class
    MyViewHolder(val binding: AdapterContatosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /*init {

            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                val usuario: Conversa = contatos.get(position)
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("chat",usuario)
                context.startActivity(intent)

            }

        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding =
            AdapterContatosBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val conversa: Conversa = conversas[position]
        holder.binding.textEmailContato.text = conversa.ultimaMsg
        val usuario: Usuario? = conversa.usuario
        holder.binding.textNomeContato.text = usuario!!.nome
        if(usuario.foto != null){
            val uri = Uri.parse(usuario.foto)
            Glide.with(context).load(uri).into(holder.binding.imageViewFotoContato)
        }else{
            holder.binding.imageViewFotoContato.setImageResource(R.drawable.padrao)
        }
    }

    override fun getItemCount(): Int {
        return conversas.size
    }
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}

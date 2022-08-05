package com.example.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsapp.R

import com.example.whatsapp.helper.UserFirebase
import com.example.whatsapp.model.Mensagem

class MensagensAdapter(
    listaMensagens: List<Mensagem>,
    var context: Context
):RecyclerView.Adapter<MensagensAdapter.MyViewHolder>() {
    private val mensagens = listaMensagens
    private val TIPO_REMETENTE = 0
    private val TIPO_DESTINATARIO = 1

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val texto: TextView = view.findViewById(R.id.textMensagemTexto)
        val foto: ImageView = view.findViewById(R.id.imageMensagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var item: View? = null
        if(viewType == TIPO_REMETENTE){
            item = LayoutInflater.from(parent.context).inflate(R.layout.adapter_mensagem_remetente,parent, false)

        }else if(viewType == TIPO_DESTINATARIO){
            item = LayoutInflater.from(parent.context).inflate(R.layout.adapter_mensagem_destinatario,parent, false)
        }
            return MyViewHolder(item!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mensagem = mensagens.get(position)
        val msg = mensagem.mensagem
        val img = mensagem.foto
        if(img != null){
            val url: Uri = Uri.parse(img)
            Glide.with(context).load(url).into(holder.foto)
            holder.texto.visibility = View.GONE
        }else{
            holder.texto.text = msg
            holder.foto.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return mensagens.size
    }
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        val mensagem = mensagens.get(position)
        val idUser = UserFirebase.IdUser()
        if (idUser.equals(mensagem.idUser)){
            return TIPO_REMETENTE
        }
            return TIPO_DESTINATARIO
    }
}

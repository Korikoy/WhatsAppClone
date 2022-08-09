package com.example.whatsapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.whatsapp.R

import com.example.whatsapp.databinding.ActivityCadastroGrupoBinding
import com.example.whatsapp.model.Usuario

class CadastroGrupoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroGrupoBinding
    private var listaEscolha: ArrayList<Usuario> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroGrupoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        if(intent.extras != null){
            val membros: ArrayList<Usuario> = intent.extras!!.getSerializable("membros") as ArrayList<Usuario>
            listaEscolha.addAll(membros)
            binding.include.textView2.text = "total ${listaEscolha.size}"
        }



    }


}
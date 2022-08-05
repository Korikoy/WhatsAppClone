package com.example.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.example.whatsapp.R
import com.example.whatsapp.config.ConfiguracaoFirebase
import com.example.whatsapp.databinding.ActivityMainBinding
import com.example.whatsapp.fragment.ContatosFragment
import com.example.whatsapp.fragment.ConversasFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var auth = ConfiguracaoFirebase.getAuth()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.include.toolbarPrincipal
        toolbar.title = "WhatsApp"
        setSupportActionBar(toolbar)

        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add("Conversas",ConversasFragment::class.java)
                .add("Contatos",ContatosFragment::class.java)
                .create()
        )

        val viewPager : ViewPager = binding.viewPager
        viewPager.adapter = adapter

        val viewPagerTab: SmartTabLayout = binding.viewPagerTab
        viewPagerTab.setViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSair ->{deslogarUsuario()
                finish()}
            R.id.menuConfiguraçoes ->{abrirConfig()}

        }
        return super.onOptionsItemSelected(item)
    }
    fun deslogarUsuario(){
        try {
            auth.signOut()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    fun abrirConfig(){
        val intent = Intent(this,ConfiguracoesActivity::class.java)
        startActivity(intent)
    }

}
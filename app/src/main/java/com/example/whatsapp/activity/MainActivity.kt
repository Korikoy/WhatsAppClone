package com.example.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
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
    lateinit var adapter: FragmentPagerItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.include.toolbarPrincipal
        toolbar.title = "WhatsApp"
        setSupportActionBar(toolbar)

        adapter = FragmentPagerItemAdapter(
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
        val menuItem = menu.findItem(R.id.menuPesquisa)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Type here to search"
        searchView.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                val fragment: ConversasFragment = adapter.getPage(0) as ConversasFragment
                fragment.recarregarConversas()

                return true
            }

        })


        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val fragment: ConversasFragment = adapter.getPage(0) as ConversasFragment
                if(p0 != null && !p0.isEmpty()){
                    fragment.pesquisarConversas(p0.toLowerCase())
                }



                return true
            }

        }

        )


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
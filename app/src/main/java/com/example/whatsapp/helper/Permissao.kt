package com.example.whatsapp.helper

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.whatsapp.activity.ConfiguracoesActivity

class Permissao {
    companion object {
        fun validarPermissoes(perm: ArrayList<String>, configuracoesActivity: ConfiguracoesActivity, request: Int): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                val listaPermissao = ArrayList<String>()
                for (perms: String in perm){
                  val temPermissao: Boolean =  ContextCompat.checkSelfPermission(configuracoesActivity,perms) == PackageManager.PERMISSION_GRANTED
                    if (!temPermissao){
                        listaPermissao.add(perms)
                    }
                }
                if (listaPermissao.isEmpty()) return true
                val novasPerms: Array<String> = listaPermissao.toTypedArray()


                ActivityCompat.requestPermissions(configuracoesActivity,novasPerms,request)


            }


            return true
        }

    }
}
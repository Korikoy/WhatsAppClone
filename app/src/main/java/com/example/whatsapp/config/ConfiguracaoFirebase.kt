package com.example.whatsapp.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

public class ConfiguracaoFirebase {
    companion object {

        fun getAuth(): FirebaseAuth{

            return FirebaseAuth.getInstance()
        }
        fun getDataRef(): DatabaseReference{

            return FirebaseDatabase.getInstance().reference
        }

        fun getStorageRef(): StorageReference {


            return FirebaseStorage.getInstance().reference
        }

    }
}
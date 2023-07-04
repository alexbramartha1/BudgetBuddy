package com.example.tugasreal

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.tugasreal.databinding.FragmentSayaBinding
import com.example.tugasreal.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Saya : Fragment() {
    val firestore = Firebase.firestore
    private var param1: String? = null
    private var param2: String? = null
    private var firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
      val binding = FragmentSayaBinding.inflate(inflater,container,false)
       binding.namaPengguna.text = firebaseAuth.currentUser?.displayName
        binding.emailUser.setText(firebaseAuth.currentUser?.email)
        binding.logout.setOnClickListener {
            prosessLogOut()
            startActivity(Intent(this.context,login::class.java))
        }

        var TAG = "firestore"
        val docRef  = firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
        docRef.get()
            .addOnSuccessListener {document ->
                if (document != null) {
                           Glide.with(this)
                               .load(document.data?.get("image"))
                               .into(binding.imageView)
                    binding.nomorPengguna.text = document.data?.get("noTelp").toString()
                    binding.editTextBirthday.setText(document.data?.get("birthDate").toString())
                    binding.editgender.setText(document.data?.get("gender").toString())
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }.addOnFailureListener {exception ->
                Log.d(TAG, "get failed with ", exception)

            }

//

        return binding.root

    }



    private fun prosessLogOut (){
        firebaseAuth.signOut()
        return
    }
}
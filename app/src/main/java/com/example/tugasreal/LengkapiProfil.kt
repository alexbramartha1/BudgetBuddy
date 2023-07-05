package com.example.tugasreal

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tugasreal.databinding.ActivityLengkapiProfilBinding
import com.example.tugasreal.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class LengkapiProfil : AppCompatActivity() {

    private lateinit var binding :ActivityLengkapiProfilBinding
    private var imageUri: Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it

        binding.uploadImage.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLengkapiProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadImage.setOnClickListener {
            selectImage.launch("image/*")
        }
        binding.simpan.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        if (binding.namalengkap.text.toString().isEmpty()
            || binding.tanggallahir.text.toString().isEmpty()
            || binding.gender.text.toString().isEmpty()
            || binding.noTelp.text.toString().isEmpty()
            || imageUri == null){
            Toast.makeText(this, "tolong isi semua kolom",Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        val storageRef = FirebaseStorage.getInstance()
            .getReference("user")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener {
                    Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUri: Uri?) {
        val data = User(
            name = FirebaseAuth.getInstance().currentUser?.displayName,
            fullName = binding.namalengkap.text.toString(),
            email = FirebaseAuth.getInstance().currentUser?.email,
            birthDate = binding.tanggallahir.text.toString(),
            gender = binding.gender.text.toString(),
            image = imageUri.toString(),
            noTelp = binding.noTelp.text.toString()
        )
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(data)
            .addOnSuccessListener {
                startActivity(Intent(this,MainActivity::class.java))
                Toast.makeText(this,"User register berhasil",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"User register gagal ",Toast.LENGTH_SHORT).show()
            }
    }

}
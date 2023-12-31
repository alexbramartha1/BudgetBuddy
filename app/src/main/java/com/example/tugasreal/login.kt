package com.example.tugasreal

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {

    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var btnRegistrasi: TextView
    lateinit var btnLogin: Button
    lateinit var progressDialog:ProgressDialog
    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editEmail = findViewById(R.id.email_login)
        editPassword = findViewById(R.id.password_login)
        btnRegistrasi = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silakan tunggu..")

         btnLogin.setOnClickListener {
          if (editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty()){
              prosesLogin()
          }else{
              Toast.makeText(this,"Silakan isi email dan password terlebih dahulu",LENGTH_SHORT).show()
          }
        }
        btnRegistrasi.setOnClickListener {
            startActivity(Intent(this,Registrasi::class.java))
        }

    }

    private fun prosesLogin(){
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                startActivity(Intent(this,MainActivity::class.java))
            }
            .addOnFailureListener { error ->
                Toast.makeText(this,error.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
    }
}
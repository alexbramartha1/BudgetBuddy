package com.example.tugasreal

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class Registrasi : AppCompatActivity() {
    lateinit var editName: EditText
    lateinit var editEmail:EditText
    lateinit var editPassword:EditText
    lateinit var editPasswordKonfirmasi:EditText
    lateinit var btnRegistrasi:Button
    lateinit var btnLogin:TextView
    lateinit var progressDialog: ProgressDialog

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)
        editName = findViewById(R.id.name_registrasi)
        editEmail = findViewById(R.id.email_registrasi)
        editPassword = findViewById(R.id.password_registrasi)
        editPasswordKonfirmasi = findViewById(R.id.konfirmasi_password_registrasi)
        btnRegistrasi = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silakan tunggu..")

        btnLogin.setOnClickListener {
            startActivity(Intent(this,login::class.java))
            finish()
        }

        btnRegistrasi.setOnClickListener {
            if (editName.text.isNotEmpty() && editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty()){
                if (editPassword.text.toString() == editPasswordKonfirmasi.text.toString()){
                    processRegister()
                }else{
                    Toast.makeText(this,"Konfirmasi kata sandi harus sama",LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Silakan isi dulu semua data ", LENGTH_SHORT).show()
            }
        }


    }

    fun processRegister(){
        var name = editName.text.toString()
        var email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    var userUpdateProfile =  UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            startActivity(Intent(this, LengkapiProfil::class.java))
                        }
                        .addOnFailureListener { error2 ->
                          Toast.makeText(this,error2.localizedMessage, LENGTH_SHORT).show()
                        }
                }else{
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(this,error.localizedMessage, LENGTH_SHORT).show()
            }
    }


}
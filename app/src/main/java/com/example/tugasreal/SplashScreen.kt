package com.example.tugasreal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Delay untuk splash screen
        Handler().postDelayed({
            // Intent untuk pindah ke activity berikutnya
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // Durasi splash screen dalam milidetik (di sini 3000 ms atau 3 detik)
    }
    }

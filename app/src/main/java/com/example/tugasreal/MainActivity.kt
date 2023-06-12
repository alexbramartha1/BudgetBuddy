package com.example.tugasreal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tugasreal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Beranda())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.beranda -> replaceFragment(Beranda())
                R.id.transaksi -> replaceFragment(Transaksi())
                R.id.artikel -> replaceFragment(Artikel())
                R.id.profil -> replaceFragment(Saya())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}

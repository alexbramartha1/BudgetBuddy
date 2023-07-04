package com.example.tugasreal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tugasreal.databinding.ActivityDetailTransaksiBinding
import com.example.tugasreal.databinding.FragmentTambahTransaksiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailTransaksi : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var exchange: Exchange
    private lateinit var binding: ActivityDetailTransaksiBinding
    private var saveState = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        user = FirebaseAuth.getInstance().currentUser!!

        val extra = intent.extras
        if (extra != null){
            exchange = Exchange(
                extra.getString("exchangeId"),
                extra.getInt("amount"),
                extra.getString("type"),
                extra.getString("category"),
                extra.getString("date"),
                extra.getString("desc"),
            )
            setExchange(exchange)
        }

        binding.deleteExchange.setOnClickListener {
            database.child("users/${user.uid}/exchange/${exchange.id}").removeValue()
            supportFragmentManager.beginTransaction().add(R.id.detailTransaksiContainer, TambahTransaksi()).commit()
        }

        binding.updateExchange.setOnClickListener {
            val fragment = Transaksi()
            val args = Bundle()
            args.putString("exchangeId", exchange.id)
            args.putInt("amount", exchange.amount!!)
            args.putString("type", exchange.type)
            args.putString("category", exchange.category)
            args.putString("date", exchange.date)
            args.putString("desc", exchange.desc)
            fragment.arguments = args
            supportFragmentManager.beginTransaction().replace(R.id.detailTransaksiContainer, fragment).commit()
        }
    }

    fun setExchange(exchange: Exchange){
        val formatter: NumberFormat = DecimalFormat("#,###")
        binding.tvJudulTransaksi.setText(exchange.desc)
        binding.tvNominal.setText("Rp. ${formatter.format(exchange.amount)}")
        binding.tvJenisTransaksi.setText(exchange.type)
        binding.tvTanggalTransaksi.setText(exchange.date)
        binding.tvKategori.setText(exchange.category)
    }
}
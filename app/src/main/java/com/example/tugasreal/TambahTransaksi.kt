package com.example.tugasreal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasreal.databinding.FragmentBerandaBinding
import com.example.tugasreal.databinding.FragmentTambahTransaksiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TambahTransaksi.newInstance] factory method to
 * create an instance of this fragment.
 */
class TambahTransaksi : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database: DatabaseReference
    private var _binding: FragmentTambahTransaksiBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        database = Firebase.database.reference
        user = FirebaseAuth.getInstance().currentUser!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTambahTransaksiBinding.inflate(inflater, container, false)
        database.child("users/${user.uid}/exchange").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("firebase", "onChildAdded:" + snapshot.value)
                displayExchanges(snapshot!!.children)
                displayStatistic(snapshot!!.children)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })

        val view = binding.root
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TambahTransaksi.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TambahTransaksi().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun displayExchanges(exchanges: Iterable<DataSnapshot>){
        binding.rvTransaksi.layoutManager = LinearLayoutManager(context)
        val data = ArrayList<Exchange>()
        exchanges.map {
            data.add(Exchange(it.key, it.getValue<Exchange>()!!.amount, it.getValue<Exchange>()!!.type, it.getValue<Exchange>()!!.category, it.getValue<Exchange>()!!.date, it.getValue<Exchange>()!!.desc))
        }
        binding.rvTransaksi.adapter = ExchangeAdapter(data)
    }

    fun displayStatistic(exchanges:Iterable<DataSnapshot>) {
        var total = 0
        var total_rate = 0
        var expanse = 0
        var expanse_rate = 0
        var expanse_index = 0
        var expanse_percentage = 0
        var income = 0
        var income_rate = 0
        var income_percentage = 0
        var income_index = 0

        exchanges.map {
            var amount = it.getValue<Exchange>()!!.amount
            var type = it.getValue<Exchange>()!!.type
            total += amount!!

            if (type == "expanse") {
                expanse += amount!!
                expanse_index++
            }

            if (type == "income") {
                income += amount!!
                income_index++
            }
        }

        if ((income_index + expanse_index) > 0) {
            total_rate = total / (income_index + expanse_index)
        }

        if (income_index > 0) {
            income_percentage = ((income.toFloat() / total.toFloat()) * 100).toInt()
            income_rate = income / income_index
        }

        if (expanse_index > 0) {
            expanse_percentage = ((expanse.toFloat() / total.toFloat()) * 100).toInt()
            expanse_rate = expanse / expanse_index
        }

        println("total : ${total}")
        println("expanse : ${expanse}")
        println("income : ${income}")
        println("expanse_percentage : ${expanse_percentage}")
        println("income_percentage : ${income_percentage}")
        println("income_rate : ${income_rate}")
        println("expanse_rate : ${expanse_rate}")
        println("total_rate : ${total_rate}")
        val formatter: NumberFormat = DecimalFormat("#,###")
        binding.textView9.text = "Rp. ${formatter.format(income)}"
        binding.textView10.text = "Rp. ${formatter.format(expanse)}"
        binding.textView7.text = "Rp. ${formatter.format(income - expanse)}"
    }
}
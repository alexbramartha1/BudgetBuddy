package com.example.tugasreal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tugasreal.databinding.FragmentTransaksiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Transaksi.newInstance] factory method to
 * create an instance of this fragment.
 */
class Transaksi : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database:DatabaseReference
    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!
    private var type:String = "income"
    private lateinit var user:FirebaseUser

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
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        binding.saveExchange.setOnClickListener {
            var amount      = Integer.parseInt(binding.amount.text.toString())
            var category    = binding.category.text.toString()
            var date        = binding.date.text.toString()
            var description = binding.description.text.toString()
            this.insert(amount, this.type, category, date, description)
        }

        binding.incomeButton.setOnClickListener {
            this.type = "income"
            println(this.type)
            this.setFeatureButton()
        }

        binding.expanseButton.setOnClickListener {
            this.type = "expanse"
            println(this.type)
            this.setFeatureButton()
        }

        val view = binding.root
        return view
    }

    fun setFeatureButton(){
        val buttons = arrayListOf(binding.incomeButton, binding.expanseButton)
        //Reset buttons theme
        for(button in buttons){
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        when(this.type){
            "income" -> {
                binding.incomeButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_light))
                binding.incomeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_dark))
            }
            "expanse" -> {
                binding.expanseButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_light))
                binding.expanseButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_dark))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Transaksi.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Transaksi().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun insert(amount: Int, type: String, category: String, date: String, desc: String){
        var exchange = Exchange(amount, type, category, date, desc)
        var key      = this.database.child("users/${user.uid}/exchange").push().getKey()
        key?.let {
            this.database.child("users/${user.uid}/exchange").child(it).setValue(exchange).addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    Toast.makeText(context, "Succcessfull saving exchange!", Toast.LENGTH_LONG)
                    println("success")
                }
            }
        }
    }
}
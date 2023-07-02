package com.example.tugasreal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasreal.databinding.FragmentArtikelBinding
import com.example.tugasreal.model.News
import com.example.tugasreal.model.SearchNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Artikel.newInstance] factory method to
 * create an instance of this fragment.
 */
class Artikel : Fragment() {

    private var _binding: FragmentArtikelBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val list = ArrayList<News>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtikelBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvArtikel.setHasFixedSize(true)
        binding.rvArtikel.layoutManager = LinearLayoutManager(context)

        RClient.instances.getNews().enqueue(object :Callback<SearchNews>{
            override fun onResponse(call: Call<SearchNews>, response: Response<SearchNews>) {
               val responseCode = response.code()
                val cekRes = response.body()?.status

                if (cekRes == "ok"){
                    response.body()?.let { list.addAll(it.articles) }
                    val adapter = NewsAdapter(list)
                    binding.rvArtikel.adapter = adapter
                }else{
                    Toast.makeText(context,"Artikel tidak di temukan",Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<SearchNews>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
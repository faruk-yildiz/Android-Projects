package com.farukyildiz.cryptoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farukyildiz.cryptoapp.R
import com.farukyildiz.cryptoapp.adapter.RecyclerViewAdapter
import com.farukyildiz.cryptoapp.databinding.ActivityMainBinding
import com.farukyildiz.cryptoapp.model.CryptoModel
import com.farukyildiz.cryptoapp.service.CryptoAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//56f1cca23e15862311c920c0733b623f2f740584
//https://rest.coinapi.io/v1/exchangerate/USD?apikey=8720A0F2-5ED8-4E74-BBD7-668EA6CCC9DC


class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener {
    private val BASE_URL="https://raw.githubusercontent.com/"
    private lateinit var cryptoModels:ArrayList<CryptoModel>
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutManager: RecyclerView.LayoutManager=LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        loadData()
    }

    private fun loadData()
    {
        val retrofit=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

        val service=retrofit.create(CryptoAPI::class.java)
        val call=service.getData()

        call.enqueue(object:Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful)
                {
                    println(response.body()!!.size)
                    response.body()?.let {

                        cryptoModels=ArrayList(it)
                        recyclerViewAdapter= RecyclerViewAdapter(cryptoModels,this@MainActivity)
                        binding.recyclerView.adapter=recyclerViewAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this@MainActivity,"${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }
}

package com.farukyildiz.cryptoapp.service

import com.farukyildiz.cryptoapp.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    //atilsamancioglu/K21-JSONDataSet/blob/master/crypto.json
    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    fun getData():Call<List<CryptoModel>>

}
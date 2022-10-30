package com.farukyildiz.landmarkbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.farukyildiz.landmarkbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var landmarkList: ArrayList<Landmark>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        landmarkList=ArrayList<Landmark>()

        val pisa=Landmark("Pısa","Italy",R.drawable.pisatower)
        val galata=Landmark("Galata Kulesi","Türkiye",R.drawable.galatakulesi)
        val colesseum=Landmark("Colesseum","Italy",R.drawable.colesseum)

        landmarkList.add(pisa)
        landmarkList.add(galata)
        landmarkList.add(colesseum)

        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        val landmarkAdapter=LandmarkAdapter(landmarkList)
        binding.recyclerView.adapter=landmarkAdapter
    }
}
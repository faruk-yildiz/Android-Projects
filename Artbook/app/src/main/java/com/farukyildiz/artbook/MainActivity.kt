package com.farukyildiz.artbook

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.farukyildiz.artbook.databinding.ActivityDetailBinding
import com.farukyildiz.artbook.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var artList:ArrayList<Art>
    private lateinit var artAdapter: ArtAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        artList= ArrayList<Art>()
        artAdapter= ArtAdapter(artList)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=artAdapter

        try {
            val database=this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)
            val cursor=database.rawQuery("SELECT * FROM arts",null)

            val nameIndex=cursor.getColumnIndex("artName")
            val idIndex=cursor.getColumnIndex("id")

            while (cursor.moveToNext())
            {
                val name=cursor.getString(nameIndex)
                val id=cursor.getInt(idIndex)

                val art=Art(name,id)
                artList.add(art)

            }
            artAdapter.notifyDataSetChanged()
            cursor.close()

        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=getMenuInflater()
        menuInflater.inflate(R.menu.art_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_item)
        {
            val intent=Intent(this@MainActivity,DetailActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
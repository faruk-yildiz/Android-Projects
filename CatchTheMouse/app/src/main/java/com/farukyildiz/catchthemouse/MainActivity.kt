package com.farukyildiz.catchthemouse

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    var runnable:Runnable= Runnable {  }
    var handler:Handler= Handler(Looper.getMainLooper())
    var playerScore:Int=0
    lateinit var random:Random
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { gameStart() }
        imageView.setOnClickListener{ scoreUp()}
    }

    fun gameStart()
    {
        playerScore=0
        if(button.isVisible) {
            button.visibility=View.GONE
        }
        object:CountDownTimer(15000,1000)
        {
            override fun onTick(p0: Long) {
                timeLeft.text="Left:${p0/1000}"
            }

            override fun onFinish() {
                handler.removeCallbacks(runnable)
                val dialog=AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("Restart")
                dialog.setMessage("Do you wanna restart")
                dialog.setPositiveButton("Yes"){dialog,which->
                    gameStart()
                }
                dialog.setNegativeButton("No"){dialog,which ->
                    finish()
                }
                dialog.show()
            }

        }.start()
        runnable=object : Runnable{
            override fun run() {
                var randX=(50..700).random().toFloat()
                var randY=(200..800).random().toFloat()
                imageView.x=randX
                imageView.y=randY
                handler.postDelayed(this,250)
            }

        }
        handler.post(runnable)

    }
    fun scoreUp()
    {
        playerScore++
        score.text="Score:$playerScore"
    }
}
package com.example.smartparking

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartparking.databinding.ActivitySplash2Binding

class Splash2 : AppCompatActivity(),SwipeListener {
    private lateinit var swipeGestureDetector: SwipeGestureDetector
    private lateinit var binding: ActivitySplash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplash2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeGestureDetector= SwipeGestureDetector(this,this)
        swipeGestureDetector.setOnTouchListener(binding.swipe)
    }

    override fun onSwipeRight() {
        (this as? Activity)?.finishAffinity()
    }

    override fun onSwipeLeft() {
        val intent=Intent(this,Splash3::class.java)
        startActivity(intent)
        finish()
    }
}
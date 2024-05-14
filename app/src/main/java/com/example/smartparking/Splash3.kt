package com.example.smartparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import com.example.smartparking.databinding.ActivitySplash3Binding

class Splash3 : AppCompatActivity(),SwipeListener {
    private lateinit var binding: ActivitySplash3Binding
    private lateinit var swipeGestureDetector: SwipeGestureDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplash3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeGestureDetector= SwipeGestureDetector(this,this)
        swipeGestureDetector.setOnTouchListener(binding.swipe2)
    }
    override fun onSwipeRight() {
        val intent = Intent(this, Splash2::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSwipeLeft() {
        val intent= Intent(this,Splash4::class.java)
        startActivity(intent)
        finish()
    }
}
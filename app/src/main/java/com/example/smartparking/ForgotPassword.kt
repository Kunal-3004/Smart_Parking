package com.example.smartparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartparking.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity(),SwipeListener {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var swipeGestureDetector: SwipeGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeGestureDetector= SwipeGestureDetector(this,this)
        swipeGestureDetector.setOnTouchListener(binding.forgotSwipe)

        binding.ResetBtn.setOnClickListener{
            val email=binding.editTextEmail.text.toString()
            if(email.isEmpty()){
                Toast.makeText(this,"Please Enter a Email address",Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this,
                                "Email sent successfully to Reset password",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        else{
                            Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
        binding.NotHaveAcc.setOnClickListener{
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onSwipeRight() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onSwipeLeft() {
    }
}
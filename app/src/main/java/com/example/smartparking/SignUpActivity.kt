package com.example.smartparking

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.smartparking.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity(),SwipeListener {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var swipeGestureDetector: SwipeGestureDetector

    private lateinit var googleSignInClient: GoogleSignInClient

   companion object {
         private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        swipeGestureDetector= SwipeGestureDetector(this,this)
        swipeGestureDetector.setOnTouchListener(binding.SignSwipe)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_sign_in_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.showPasswordToggleButton.setOnCheckedChangeListener{ buttonView,isChecked->
            if(isChecked){
                binding.passwordEditText.inputType=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            else{
                binding.passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        binding.alreadyAccount.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.googleBtn.setOnClickListener{
            signInWithGoogle()
        }
        binding.Login.setOnClickListener{
            val  email=binding.editTextEmail.text.toString()
            val password=binding.passwordEditText.text.toString()
            if(checkAllField()){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Account created successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,map::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Account already exist!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun checkAllField(): Boolean{
        val email=binding.editTextEmail.text.toString()
        if(binding.editTextName.text.toString()==""){
            binding.editTextName.error="This is a required field"
            return false
        }
        if(email==""){
            binding.editTextEmail.error="This is a required field"
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editTextEmail.error="Check email format"
            return false
        }
        if(binding.passwordEditText.text.toString()==""){
            binding.passwordEditText.error="This is a required field"
            return false
        }
        if(binding.passwordEditText.length() <= 6) {
            binding.passwordEditText.error = "Password should atleast 8 characters long"
            return false
        }
        if(binding.confirmPasswordEditText.text.toString()==""){
            binding.confirmPasswordEditText.error="This is a required field"
            return false
        }
        if(binding.confirmPasswordEditText.text.toString()!=binding.passwordEditText.text.toString()){
            binding.confirmPasswordEditText.error="Password not matched"
            return false
        }
        return true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, map::class.java)
                    startActivity(intent)
                    finish()
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    val message = task.exception?.message ?: "Authentication failed."
                    Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onSwipeRight() {
        val intent = Intent(this, Splash4::class.java)
        startActivity(intent)
        finish()
    }
    override fun onSwipeLeft() {
    }
}
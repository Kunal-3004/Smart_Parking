package com.example.smartparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.widget.Toast
import android.provider.Settings
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.smartparking.databinding.ActivitySplash4Binding

class Splash4 : AppCompatActivity(),SwipeListener {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private lateinit var swipeGestureDetector: SwipeGestureDetector
    private lateinit var binding: ActivitySplash4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplash4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeGestureDetector= SwipeGestureDetector(this,this)
        swipeGestureDetector.setOnTouchListener(binding.swipe3)

        binding.NotNow.setOnClickListener{
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.EnableLocation.setOnClickListener {
            enableLocation(it)
        }
        /*if (isLocationPermissionGranted() && isLocationEnabled()) {
            startNextActivity()
        }*/
    }
    override fun onSwipeRight() {
        val intent = Intent(this, Splash3::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSwipeLeft() {
    }
    private fun isLocationPermissionGranted():Boolean{
        return ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION
        )==PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationMode: Int = try {
            Settings.Secure.getInt(
                applicationContext.contentResolver,
                Settings.Secure.LOCATION_MODE
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    fun enableLocation(view: android.view.View) {
        if (!isLocationEnabled()) {
            val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(locationSettingsIntent)
        } else {
            startNextActivity()
        }
    }
    private fun startNextActivity() {
        if (isLocationPermissionGranted()) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
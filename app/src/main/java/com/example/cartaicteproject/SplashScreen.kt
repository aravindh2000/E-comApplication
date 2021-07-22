package com.example.cartaicteproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        loading()
    }
    fun loading()
    {
        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            Handler().postDelayed({startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()},1500)

        }
        else{
            Handler().postDelayed({startActivity(Intent(this@SplashScreen, CreateAccount::class.java))
            finish()},1500)


        }

    }
}
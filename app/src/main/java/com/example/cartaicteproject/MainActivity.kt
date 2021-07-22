package com.example.cartaicteproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cartaicteproject.fragment.AddFragment
import com.example.cartaicteproject.fragment.HomeFragment
import com.example.cartaicteproject.fragment.NotificationFragment
import com.example.cartaicteproject.fragment.Profile_frg
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val clicked= BottomNavigationView.OnNavigationItemSelectedListener {
            item ->
        when(item.itemId)
        {
            R.id.profile ->{
                Navigate(Profile_frg())
                return@OnNavigationItemSelectedListener true }
            R.id.home->
            {
                Navigate(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_add->{
                Navigate(AddFragment())
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    private fun Navigate(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().replace(R.id.containerframe,fragment).commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomview:BottomNavigationView=findViewById(R.id.btnav)
        bottomview.setOnNavigationItemSelectedListener(clicked)
        Navigate(HomeFragment())


    }
  
}
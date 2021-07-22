package com.example.cartaicteproject

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var email_login:EditText
    lateinit var password_login:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login:Button=findViewById(R.id.login)
         email_login=findViewById(R.id.email_login)
        password_login=findViewById(R.id.password_login)
        login.setOnClickListener {
            LoginUser()
        }
    }
    private fun LoginUser()
    {
        val EmailforUser=email_login.text.toString()
        val Pass=password_login.text.toString()
        when {
            TextUtils.isEmpty(EmailforUser)-> Toast.makeText(this,"Enter valid email", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(Pass)-> Toast.makeText(this,"Enter vaid password", Toast.LENGTH_LONG).show()
            else->
            {
                val progressDialog= ProgressDialog(this)
                progressDialog.setTitle("Signing In")
                progressDialog.setMessage("Please Wait Logging in..")
                progressDialog.show()
                val firebase= FirebaseAuth.getInstance()
                firebase.signInWithEmailAndPassword(EmailforUser,Pass).addOnCompleteListener {
                        task->
                    if(task.isSuccessful)
                    {
                        progressDialog.dismiss()
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }
                    else{
                        progressDialog.dismiss()
                        val msg=task.exception!!.toString()
                        Toast.makeText(this,"Error : unable to login due to ${msg}", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }
}
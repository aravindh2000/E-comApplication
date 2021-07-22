package com.example.cartaicteproject

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccount : AppCompatActivity() {
    lateinit var register:Button
    lateinit var loginbtn:TextView
    lateinit var email:EditText
    lateinit var name:EditText
    lateinit var password:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        register=findViewById(R.id.reg)
        loginbtn=findViewById(R.id.loginbtn)
        email=findViewById(R.id.email)
        name=findViewById(R.id.name)
        password=findViewById(R.id.password)
        loginbtn.setOnClickListener {
            val intent=Intent(this@CreateAccount,LoginActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            createaccount()


        }



    }
    private  fun createaccount()
    {

        val UserName=name.text.toString()
        val EmailId=email.text.toString()
        val Password=password.text.toString()
        when{
            TextUtils.isEmpty(UserName)-> Toast.makeText(this,"please enter the valid user name",
                Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(EmailId)-> Toast.makeText(this,"please enter valid Email id", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(Password)-> Toast.makeText(this,"please enter valid password id",
                Toast.LENGTH_LONG).show()
            else->{
                val progressDialog= ProgressDialog(this)
                progressDialog.setTitle("Signing Up")
                progressDialog.setMessage("This will take a while")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val fb= FirebaseAuth.getInstance()
                fb.createUserWithEmailAndPassword(EmailId,Password).addOnCompleteListener { task->
                    if(task.isSuccessful)
                    {
                        StoreUser(UserName,EmailId,progressDialog)
                    }
                    else{progressDialog.dismiss()
                        val errormsg=task.exception!!.toString()
                        Toast.makeText(this,"Err : ${errormsg}", Toast.LENGTH_LONG).show()
                        fb.signOut()

                    }
                }

            }


        }
    }

    private fun StoreUser(UserName:String,EmailId:String,progressDialog: ProgressDialog) {
        val currentuser= FirebaseAuth.getInstance().currentUser!!.uid
        val databaseref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("UserDetails")
        val storeval=HashMap<String,String>()
        storeval.put("name",UserName.toLowerCase())
        storeval.put("email",EmailId)
        storeval.put("uid",currentuser)
        databaseref.child(currentuser).setValue(storeval).addOnCompleteListener { task ->
            if(task.isSuccessful)
            {progressDialog.dismiss()
                startActivity(Intent(this@CreateAccount, MainActivity::class.java))
                finish()
            }
            else{
                progressDialog.dismiss()
                val errormg=task.exception!!.toString()
                Toast.makeText(this,"Error: ${errormg}", Toast.LENGTH_LONG).show()

            }
        }

    }

}
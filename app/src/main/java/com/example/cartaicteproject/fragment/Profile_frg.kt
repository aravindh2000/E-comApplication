package com.example.cartaicteproject.fragment

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartaicteproject.Adopter.AdapterClass
import com.example.cartaicteproject.Adopter.Data
import com.example.cartaicteproject.Adopter.UploadAdapter
import com.example.cartaicteproject.Adopter.UploadData
import com.example.cartaicteproject.LoginActivity
import com.example.cartaicteproject.MainActivity
import com.example.cartaicteproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.util.*


class Profile_frg : Fragment() {
  lateinit var logout:Button
    var count :Int?=null
    private  var userAdapter:UploadAdapter?=null
private var listOfItems:ArrayList<UploadData>?=null
    lateinit var followersCount:TextView
    lateinit var followingCount :TextView
    lateinit var edit_button:Button

    lateinit var user_info:TextView
    lateinit var emailId:TextView
    private   var firebaseUser: FirebaseUser?=null
  private lateinit  var profileId:String
    private lateinit var databaseReference: DatabaseReference
   private var recyclerView: RecyclerView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_profile_frg, container, false)
        emailId=view.findViewById(R.id.product)
        logout=view.findViewById(R.id.logout)
        edit_button=view.findViewById(R.id.edit)

        followersCount=view.findViewById(R.id.followersCount)
        followingCount=view.findViewById(R.id.followingCount)
        user_info=view.findViewById(R.id.user)
        recyclerView=view.findViewById(R.id.scroll)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager=LinearLayoutManager(context)
        listOfItems=ArrayList()

          firebaseUser=FirebaseAuth.getInstance().currentUser!!
        databaseReference=FirebaseDatabase.getInstance().reference.child("UserDetails").child(firebaseUser!!.uid).child("Images")
        recyclerView?.adapter=userAdapter
        val pref=context?.getSharedPreferences("pref",Context.MODE_PRIVATE)
        if(pref!=null)
        {
           this.profileId=pref.getString("proId","none")!!

        }
        if(profileId==firebaseUser!!.uid)
        {
            edit_button.text="Product Nos"
            databaseReference.addValueEventListener(object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    count=snapshot.childrenCount.toInt()
                    for (i in snapshot.children)
                    {
                        for ( j in i.children)
                        {   val amount= j.key.toString()
                            var dataUse=j.value.toString()

                            listOfItems!!.add(UploadData(dataUse,amount))
                        }
                    }
                    recyclerView?.adapter=UploadAdapter(context!!,listOfItems!!)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
        else if (profileId !=firebaseUser!!.uid)
        {
            buttonStatus()
            dataOfUser()
        }
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context,LoginActivity::class.java))
            MainActivity().finish()

        }
              edit_button.setOnClickListener {
            Toast.makeText(context,"Total : "+count.toString()+" Products",Toast.LENGTH_LONG).show()
        }
        followinCount()
        followerCount()
        info()

       return view
    }


    private fun dataOfUser() {
        databaseReference=FirebaseDatabase.getInstance().reference.child("UserDetails").child(profileId).child("Images")
        databaseReference.addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                count=snapshot.childrenCount.toInt()
                for (i in snapshot.children)
                {
                    for ( j in i.children)
                    {   val amount= j.key.toString()
                        var dataUse=j.value.toString()

                        listOfItems!!.add(UploadData(dataUse,amount))
                    }
                }
                recyclerView?.adapter=UploadAdapter(context!!,listOfItems!!)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


    private fun info() {
        val infoOfUser=FirebaseDatabase.getInstance().getReference().child("UserDetails").child(profileId)
        infoOfUser.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists())
                {
                    val users=snapshot.getValue(Data::class.java)

                    user_info.text=users!!.getName().toUpperCase()
                    emailId.text=users!!.getEmail().toLowerCase()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun buttonStatus() {
        val followAndFollowingCount= FirebaseDatabase.getInstance().reference.child("ADD").child(profileId)
                    .child("ADDED")

        if(followAndFollowingCount!=null)
        {
            followAndFollowingCount.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.child(profileId).exists())
                    {
                        edit_button.text="ADD"
                    }
                    else{
                        edit_button.text="ADDED"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


    private fun followerCount()
    {
        val ct= FirebaseDatabase.getInstance().reference.child("ADD")
                    .child(profileId).child("Followers")

        ct.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    followersCount.text=snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun followinCount()
    {
        val ctf= firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference.child("ADD")
                    .child(it1.toString()).child("ADDED")
        }
        ctf.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    followingCount.text=snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onStop() {
        super.onStop()
        val pre = context?.getSharedPreferences("pref", Context.MODE_PRIVATE)?.edit()
        pre?.putString("proId", firebaseUser!!.uid)
        pre?.apply()
    }

    override fun onPause() {
        super.onPause()

        val pre = context?.getSharedPreferences("pref", Context.MODE_PRIVATE)?.edit()
        pre?.putString("proId", firebaseUser!!.uid)
        pre?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pre = context?.getSharedPreferences("pref", Context.MODE_PRIVATE)?.edit()
        pre?.putString("proId", firebaseUser!!.uid)
        pre?.apply()
    }
}

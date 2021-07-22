package com.example.cartaicteproject.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cartaicteproject.Adopter.*
import com.example.cartaicteproject.R
import com.google.firebase.database.*
import org.w3c.dom.Text


class HomeFragment : Fragment() {
lateinit var search_Box:EditText
     lateinit var progressBar: ProgressBar
    private var listOfItems:ArrayList<ImageData>?=null
     private var dataref2:DatabaseReference?=null
    private var recyclerView: RecyclerView? = null
    private var recycler2:RecyclerView?=null
    private var userAdapter: AdapterClass? = null
    private var list: MutableList<Data>?=null
    private  var dataretrieve:DatabaseReference?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val view=inflater.inflate(R.layout.fragment_home, container, false)
search_Box=view.findViewById(R.id.search_column)
       progressBar=view.findViewById(R.id.progressBar)
       listOfItems= ArrayList()
        dataref2=FirebaseDatabase.getInstance().reference.child("UserDetails")
        recycler2=view.findViewById(R.id.r2)
        dataretrieve=FirebaseDatabase.getInstance().reference.child("UserData")
        recyclerView=view.findViewById(R.id.rec1)
        recycler2?.setHasFixedSize(true)
        recycler2?.layoutManager=LinearLayoutManager(context)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager= LinearLayoutManager(context)
        list=ArrayList()
        userAdapter=context?.let { AdapterClass(it,list as ArrayList<Data>,true) }
        recyclerView?.adapter=userAdapter
        userDataDisplay()
        search_Box.setOnClickListener {
            recycler2?.visibility=View.INVISIBLE
            progressBar.visibility=View.VISIBLE
            search_Box.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(search_Box.text.toString()=="")
                    {

                    }
                    else{
                        recyclerView?.visibility=View.VISIBLE
                        retrieveUsers()
                        searchUsers(p0.toString().toLowerCase())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })


        }





        return view
    }

    private fun userDataDisplay() {

        recycler2?.visibility=View.VISIBLE
        progressBar.visibility=View.VISIBLE
        dataretrieve?.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                 for (i in snapshot.children)
                 {
                     for (j in i.children)
                     {
                         val c=j.key.toString()
                         val a=j.value.toString()
                         listOfItems?.add(ImageData(a,c))

                     }
                 }
                progressBar.visibility=View.INVISIBLE
                recycler2?.adapter=DataAdopterClass(context!!,listOfItems!!)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



    }

    private fun searchUsers(input:String)
    {
        val quer= FirebaseDatabase.getInstance().getReference().child("UserDetails").orderByChild("name").startAt(input).endAt(input+ "\uf8ff ")
        quer.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                list?.clear()


                for(s in snapshot.children )
                {
                    val use =s.getValue(Data::class.java)
                    if(use != null)
                    {
                        list?.add(use)
                    }
                }
                userAdapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun retrieveUsers()
    {
        val uRef= FirebaseDatabase.getInstance().getReference().child("UserDetails")
        uRef.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(search_Box.text.toString()=="")
                {
                    list?.clear()
                    for(s in snapshot.children )
                    {
                        val use =s.getValue(Data::class.java)
                        if(use != null)
                        {
                            list?.add(use)
                        }
                    }
                    progressBar.visibility=View.INVISIBLE
                    userAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}
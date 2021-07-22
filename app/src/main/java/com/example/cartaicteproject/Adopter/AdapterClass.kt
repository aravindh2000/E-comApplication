package com.example.cartaicteproject.Adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cartaicteproject.R
import com.example.cartaicteproject.fragment.Profile_frg
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.annotations.NotNull

class AdapterClass(private var mContext: Context, private var list:List<Data>, private var isFragment:Boolean=false):
    RecyclerView.Adapter<AdapterClass.ViewHolder>() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.model_view, parent, false)
        return AdapterClass.ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = list[position]
        holder.user_text.text = pos.getName()
        checkFollowingStatus(pos.getUid(), holder.btnForFollow)
        holder.itemView.setOnClickListener { View.OnClickListener {
            val prefval=mContext.getSharedPreferences("pref",Context.MODE_PRIVATE).edit()
            prefval.putString("proId",pos.getUid())
            prefval.apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.containerframe,Profile_frg()).commit()
        } }
        holder.itemView.setOnClickListener(View.OnClickListener {
            val pre = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE).edit()
            pre.putString("proId", pos.getUid())
            pre.apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.containerframe, Profile_frg()).commit()
        })
        holder.btnForFollow.setOnClickListener {
            if (holder.btnForFollow.text.toString() == "ADD") {
                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("ADD").child(it1.toString())
                        .child("ADDED").child(pos.getUid())
                        .setValue(true).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                firebaseUser?.uid.let { it1 ->


                                    FirebaseDatabase.getInstance().reference.child("ADD")
                                        .child(pos.getUid()).child("Followers")
                                        .child(it1.toString())
                                        .setValue(true).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

                                            }
                                        }
                                }
                            }
                        }
                }

            } else {

                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference.child("ADD").child(it1.toString())
                        .child("ADDED").child(pos.getUid())
                        .removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                firebaseUser?.uid.let { it1 ->


                                    FirebaseDatabase.getInstance().reference.child("ADD")
                                        .child(pos.getUid()).child("Followers")
                                        .child(it1.toString())
                                        .removeValue().addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

                                            }
                                        }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun checkFollowingStatus(uid: String, btnForFollow: Button) {
        val fref = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference.child("ADD").child(it1.toString())
                .child("ADDED")

        }
        fref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(uid).exists()) {
                    btnForFollow.text = "ADDED"

                } else {
                    btnForFollow.text = "ADD"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(@NotNull itm_view: View) : RecyclerView.ViewHolder(itm_view) {
        var user_text: TextView = itm_view.findViewById(R.id.user_name)
        var btnForFollow: Button = itm_view.findViewById(R.id.follow)

    }
}

package com.example.cartaicteproject.Adopter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cartaicteproject.R
import com.example.cartaicteproject.fragment.NotificationFragment
import com.example.cartaicteproject.fragment.Profile_frg
import com.squareup.picasso.Picasso

 class UploadAdapter (private val stateOfFragmet: Context,private val listOfUploads:ArrayList<UploadData>):
    RecyclerView.Adapter<UploadAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view=LayoutInflater.from(stateOfFragmet).inflate(R.layout.product_image,parent,false)
        return UploadAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val listItems=listOfUploads[position]

        Picasso.get().load(listItems.path()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageForDisplay)
        holder.textView.text=listItems.amountOfGood()
       holder.orderBtn.setOnClickListener {
           (stateOfFragmet as FragmentActivity).supportFragmentManager.beginTransaction()
               .replace(R.id.containerframe, NotificationFragment()).commit()
       }

    }

    override fun getItemCount(): Int {

        return listOfUploads.size
    }

     class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val imageForDisplay:ImageView=view.findViewById(R.id.display)
       val textView:TextView=view.findViewById(R.id.cost)
      val orderBtn:Button=view.findViewById(R.id.btnForOrder)

    }

}

package com.example.cartaicteproject.Adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cartaicteproject.R
import com.squareup.picasso.Picasso

class DataAdopterClass(private val mContext: Context, private val listOfItems:ArrayList<ImageData>):RecyclerView.Adapter<DataAdopterClass.dataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dataViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.model_users,parent,false)
        return DataAdopterClass.dataViewHolder(view)
    }

    override fun onBindViewHolder(holder: dataViewHolder, position: Int) {
         val obj=listOfItems[position]
        holder.nameOfUser.text=obj.users()
        Picasso.get().load(obj.imageUrl).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageToDisplay)
    }

    override fun getItemCount(): Int {
       return listOfItems.size
    }

    class dataViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val imageToDisplay:ImageView=view.findViewById(R.id.iv)
        val nameOfUser:TextView=view.findViewById(R.id.sellerName)
    }
}
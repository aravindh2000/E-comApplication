package com.example.cartaicteproject.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.cartaicteproject.R

class NotificationFragment : Fragment() {
   lateinit var toMsg:EditText
    lateinit var disMsg:EditText
    lateinit var sub: EditText
    lateinit var sendBtn:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_notification, container, false)

        toMsg=view.findViewById(R.id.to)
        disMsg=view.findViewById(R.id.msg)
        sub=view.findViewById(R.id.sub)

        sendBtn=view.findViewById(R.id.send)
       sendBtn.setOnClickListener {
            val toAdd=toMsg.text.toString().trim()
           val subj=sub.text.toString().trim()
           val dis=disMsg.text.toString().trim()
           sendMail(toAdd,subj,dis)
        }
    return view
    }

    private fun sendMail(toAdd: String, subj: String, dis: String) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.data=Uri.parse("mailto: ")
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL,toAdd)
        intent.putExtra(Intent.EXTRA_SUBJECT,subj)
        intent.putExtra(Intent.EXTRA_TEXT,dis)
        try {
            startActivity(Intent.createChooser(intent,"email client.."))
        }
        catch (e:Exception)
        {
            Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
        }


    }


}
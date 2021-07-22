package com.example.cartaicteproject.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import com.example.cartaicteproject.Adopter.UploadData
import com.example.cartaicteproject.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.*
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class AddFragment : Fragment() {

    lateinit var choose:Button
    lateinit var upload:Button
    lateinit var pb:ProgressBar
    lateinit var proId:EditText
    lateinit var productId:EditText

   private lateinit var imageView:ImageView
  private var imageUri: Uri?=null
  private var firebaseUser:FirebaseUser?=null
    val IMAGE_REQUEST:Int=1
private var dataref:DatabaseReference?=null
    private var imgDataBase:DatabaseReference?=null
 private  var storageReference: StorageReference?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val  view= inflater.inflate(R.layout.fragment_add, container, false)
        choose=view.findViewById(R.id.choose_button)
        productId=view.findViewById(R.id.pid)
        upload=view.findViewById(R.id.upload_button)
        pb=view.findViewById(R.id.progressBar2)
        proId=view.findViewById(R.id.product_id)

        imageView=view.findViewById(R.id.image_upload)
        firebaseUser=FirebaseAuth.getInstance().currentUser!!
        imgDataBase=FirebaseDatabase.getInstance().reference.child("UserData")
        dataref=FirebaseDatabase.getInstance().reference.child("UserDetails").child(firebaseUser!!.uid).child("Images")
        storageReference=FirebaseStorage.getInstance().reference.child("Uploads")
   upload.setOnClickListener {

        imageUpload()
   }
        choose.setOnClickListener {
            openFolder()

        }

    return view
    }




    private fun openFolder()
{
    val intent= Intent()

    intent.type="image/*"
    intent.action=Intent.ACTION_GET_CONTENT
    startActivityForResult(intent,IMAGE_REQUEST)

}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null)
        {
            imageUri=data.data!!
            Picasso.get().load(imageUri).into(imageView)

        }
    }
    private fun imageUpload() {

        if(imageUri!=null)

        {
            pb.visibility=View.VISIBLE
            val sr=storageReference!!.child(System.currentTimeMillis().toString()+"."+"jpeg")
            var uploadTask:StorageTask<*>
            uploadTask=sr.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>> {
                task ->  if(!task.isSuccessful)
            {
                task.exception?.let {
                    throw it
                }
            }
                return@Continuation sr.downloadUrl
            }).addOnCompleteListener {
                task->
                if(task.isSuccessful)
                {  pb.visibility=View.INVISIBLE
                    Toast.makeText(context,"Successfully Uploaded",Toast.LENGTH_LONG).show()

                    val downloadUrl=task.result
                    val url=downloadUrl.toString()
                    val dataOfrate=proId.text.toString()+"   "+productId.text.toString()

                   val hashMap=HashMap<String,String>()
                    val hashMapUserDet=HashMap<String,String>()
                    hashMapUserDet.put(dataOfrate,url)
                    hashMap.put(dataOfrate,url)

                    val uniId=dataref?.push()?.key
                    imgDataBase?.child(uniId!!)?.setValue(hashMapUserDet)
                    dataref?.child(uniId!!)?.setValue(hashMap)

                }
            }
        }


        }

}
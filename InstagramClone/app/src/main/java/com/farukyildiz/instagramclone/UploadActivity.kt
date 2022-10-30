package com.farukyildiz.instagramclone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.farukyildiz.instagramclone.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.security.Permission
import java.security.Timestamp
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUploadBinding
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri?=null
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage:FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUploadBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        register()
        auth= Firebase.auth
        firestore=Firebase.firestore
        storage=Firebase.storage
    }
    fun selectImage(view:View)
    {
        if(ContextCompat.checkSelfPermission(this@UploadActivity,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Snackbar.make(view,"Permission needed",Snackbar.LENGTH_INDEFINITE).setAction("Give access")
                {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //request permisson
                }.show()
            }
            else
            {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            //request permisson
            }
        }
        else{
            val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }
    fun upload(view: View)
    {
        val uuid=UUID.randomUUID()
        val imageName="$uuid"
        val reference=storage.reference
        val imageReference=reference.child("images/$imageName")

        if(selectedPicture!=null)
        {
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                val uploadPictureReference=imageReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl=it.toString()

                    if(auth.currentUser!=null)
                    {
                        val postMap= hashMapOf<String,Any>()
                        postMap.put("downloadUrl",downloadUrl)
                        postMap.put("userEmail",auth.currentUser!!.email!!)
                        postMap.put("comment",binding.commentText.text.toString())
                        postMap.put("date",com.google.firebase.Timestamp.now())

                        firestore.collection("posts").add(postMap).addOnSuccessListener {
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }.addOnFailureListener{
                Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun register()
    {
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result->
                if(result.resultCode== RESULT_OK)
                {
                    val intentFromResult=result.data
                    if(intentFromResult!=null)
                    {
                        selectedPicture=intentFromResult.data
                        selectedPicture?.let{
                            binding.imageView.setImageURI(it)
                        }
                    }
                }
            })
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission(),)
        {
            result->
            if (result)
            {
                val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                Toast.makeText(this@UploadActivity,"Permisson needed",Toast.LENGTH_LONG).show()
            }
        }
    }
}
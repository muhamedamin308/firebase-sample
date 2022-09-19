package com.rkpandey.firebaseapp

import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class MainActivity:AppCompatActivity() {
    private val REQUEST_CODE_IMAGE = 101
    private lateinit var firebaseReference : DatabaseReference
    private lateinit var storageReference : StorageReference

    private lateinit var addImageView : ImageView
    private lateinit var addImageName : EditText
    private lateinit var tvProgress : TextView
    private lateinit var progressBar : ProgressBar
    private lateinit var uploadImage : ImageView
    private lateinit var turnback : ImageView
    private lateinit var imageUrl : Uri
    private var isImageAdded = false
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addImageName = findViewById(R.id.imageName)
        addImageView = findViewById(R.id.addNewImage)
        uploadImage = findViewById(R.id.uploadPhoto)
        tvProgress = findViewById(R.id.textProgress)
        progressBar = findViewById(R.id.progressBar)
        turnback = findViewById(R.id.ivTurnBack)

        tvProgress.visibility = View.GONE
        progressBar.visibility = View.GONE

        firebaseReference = FirebaseDatabase.getInstance().reference.child("Icon")
        storageReference = FirebaseStorage.getInstance().reference.child("IconImage")

        uploadImage.setOnClickListener {
            val imageName = addImageName.text.toString()
            if(isImageAdded && imageName.isNotBlank()){
                UploadImage(imageName)
            }
        }

        addImageView.setOnClickListener{
            val intent : Intent = Intent()
            intent.type="image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent , REQUEST_CODE_IMAGE)
        }

        turnback.setOnClickListener{
            onBackPressed()
        }
    }

    private fun UploadImage(imageName : String) {
        tvProgress.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        val key = firebaseReference.push().key
        storageReference.child("$key.jpg").putFile(imageUrl).addOnSuccessListener {

            storageReference.child("$key.jpg").downloadUrl.addOnSuccessListener {
                val hashMap = HashMap<String , Any>()
                hashMap["ImageName"]= imageName
                hashMap["ImageUrl"]= it.toString()

                firebaseReference.child(key!!).setValue(hashMap).addOnSuccessListener {
                    Toast.makeText(this , "Dat Success Uploaded", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext , HomeActivity::class.java))
                    addImageName.text.clear()
                }
            }

        }.addOnProgressListener {
            val progress : Double = ((it.bytesTransferred * 100) / it.totalByteCount).toDouble()
            progressBar.progress = progress.toInt()
            tvProgress.text = "$progress %"
        }
    }

    override fun onActivityResult(requestCode : Int , resultCode : Int , data : Intent?) {
        super.onActivityResult(requestCode , resultCode , data)
        if(requestCode == REQUEST_CODE_IMAGE && data != null){
            imageUrl = data.data!!
            isImageAdded = true
            addImageView.setImageURI(imageUrl)
        }
    }
}
package com.rkpandey.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class DetailsActivity:AppCompatActivity() {
    private lateinit var imageSrcUrl : ImageView
    private lateinit var iconName : TextView
    private lateinit var deleteIcon : ImageView
    private lateinit var undo : ImageView
    private lateinit var dataRef : DatabaseReference
    private lateinit var ref : DatabaseReference
    private lateinit var storageRef : StorageReference
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        dataRef = FirebaseDatabase.getInstance().reference.child("Icon")
        imageSrcUrl = findViewById(R.id.iconSrcUrl)
        iconName = findViewById(R.id.textIconName)
        deleteIcon = findViewById(R.id.deleteIcon)
        undo = findViewById(R.id.ivUndo)

        val iconKey = intent.getStringExtra("IconKey")
        ref = FirebaseDatabase.getInstance().reference.child("Icon").child(iconKey!!)
        storageRef = FirebaseStorage.getInstance().reference.child("IconImage").child("$iconKey.jpg")

        dataRef.child(iconKey).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    val imageName : String = snapshot.child("ImageName").value.toString()
                    val imageUri : String = snapshot.child("ImageUrl").value.toString()
                    Picasso.get().load(imageUri).into(imageSrcUrl)
                    iconName.text = imageName
                }
            }
        })
        undo.setOnClickListener {
            onBackPressed()
        }
        deleteIcon.setOnClickListener {
            ref.removeValue().addOnSuccessListener {
                storageRef.delete().addOnSuccessListener {
                    startActivity(Intent(applicationContext , HomeActivity::class.java))
                }
            }
        }
    }
}
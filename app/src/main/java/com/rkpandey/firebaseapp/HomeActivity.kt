package com.rkpandey.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.squareup.picasso.Picasso

class HomeActivity:AppCompatActivity() {
    private lateinit var inputSearch : EditText
    private lateinit var recyclerView : RecyclerView
    private lateinit var addImage : ImageView
    //Firebase
    private lateinit var options : FirebaseRecyclerOptions<Icon>
    private lateinit var adapter : FirebaseRecyclerAdapter<Icon , MyViewHolder>
    private lateinit var dataRef : DatabaseReference
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        dataRef = FirebaseDatabase.getInstance().reference.child("Icon")
        inputSearch = findViewById(R.id.searchIcons)
        recyclerView = findViewById(R.id.iconsRecyclerView)
        addImage = findViewById(R.id.ivAddImage)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        addImage.setOnClickListener {
            startActivity(Intent(applicationContext , MainActivity::class.java))
        }

        LoadData("")

        inputSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0 : Editable?) {
                if(p0?.toString() != null){
                    LoadData(p0.toString())
                }else {
                    LoadData("")
                }
            }
            override fun beforeTextChanged(p0 : CharSequence? , p1 : Int , p2 : Int , p3 : Int) {

            }

            override fun onTextChanged(p0 : CharSequence? , p1 : Int , p2 : Int , p3 : Int) {

            }
        })
    }
    private fun LoadData(data : String){
        var query : Query = dataRef.orderByChild("ImageName").startAt(data).endAt(data+"\uf8ff")

        options = FirebaseRecyclerOptions.Builder<Icon>().setQuery(query , Icon::class.java).build()

        adapter = object : FirebaseRecyclerAdapter<Icon , MyViewHolder>(options) {
            override fun onBindViewHolder(
                    holder : MyViewHolder ,
                    position : Int ,
                    model : Icon) {
                holder.iconName.text = model.ImageName
                Picasso.get().load(model.ImageUrl).into(holder.iconSrc)
                holder.view.setOnClickListener{
                    val intent = Intent(this@HomeActivity , DetailsActivity::class.java)
                    intent.putExtra("IconKey" , getRef(position).key)
                    startActivity(intent)
                }
            }

            override fun onCreateViewHolder(
                    parent : ViewGroup ,
                    viewType : Int) : MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.icons_item , parent , false)
                return MyViewHolder(view)
            }
        }

        adapter.startListening()
        recyclerView.adapter = adapter
    }
}

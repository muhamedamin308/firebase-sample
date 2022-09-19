package com.rkpandey.firebaseapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class MyViewHolder(@NonNull itemView : View):RecyclerView.ViewHolder(itemView)
{
    var iconSrc : ImageView
    var iconName : TextView
    var view : View
    init {
        iconSrc = itemView.findViewById(R.id.iconSrc)
        iconName = itemView.findViewById(R.id.iconName)
        view = itemView
    }
}

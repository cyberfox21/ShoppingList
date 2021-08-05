package com.tatyanashkolnik.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tatyanashkolnik.shoppinglist.R

class ShopListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.tv_name)
    val count = view.findViewById<TextView>(R.id.tv_count)
}
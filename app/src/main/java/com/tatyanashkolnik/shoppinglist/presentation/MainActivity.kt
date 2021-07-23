package com.tatyanashkolnik.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.tatyanashkolnik.shoppinglist.R
import com.tatyanashkolnik.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var ll_shopList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ll_shopList = findViewById(R.id.ll_shopList)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
            showList(it)
        }
    }

    private fun showList(shopList: List<ShopItem>) {
        ll_shopList.removeAllViews()
        for(shopItem in shopList){
            val id : Int = if(shopItem.enabled) R.layout.item_shop_enabled
                           else R.layout.item_shop_disabled
            val view = LayoutInflater.from(this).inflate(id, ll_shopList, false)
            val name = view.findViewById<TextView>(R.id.tv_name)
            val count = view.findViewById<TextView>(R.id.tv_count)
            name.text = shopItem.name
            count.text = shopItem.count.toString()
            view.setOnLongClickListener {
                viewModel.changeEnableState(shopItem)
                true
            }
            ll_shopList.addView(view)
        }
    }
}
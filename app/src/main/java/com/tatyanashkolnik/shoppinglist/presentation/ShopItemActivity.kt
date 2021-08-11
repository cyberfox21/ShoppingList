package com.tatyanashkolnik.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tatyanashkolnik.shoppinglist.R

class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        Log.d("CHECKER", "extra_mode $mode")
    }

    companion object{

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"

        fun newIntentAddItem(context: Context): Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent{ // параметр shopItemId
            val intent = Intent(context, ShopItemActivity::class.java) // обязательный, если нужно
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT) // запустить экран в режиме редактирования
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }

    }
}
package com.tatyanashkolnik.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tatyanashkolnik.shoppinglist.R
import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import com.tatyanashkolnik.shoppinglist.presentation.ShopListAdapter.Companion.MAX_POOL_SIZE

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var ll_shopList: LinearLayout
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        ll_shopList = findViewById(R.id.ll_shopList)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it // проделывает вычисления в главном потоке
            // и после устанавливает список в адаптер
        }
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        with(rv) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rv)
    }

    private fun setupSwipeListener(rv: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.shopList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        })
        itemTouchHelper.attachToRecyclerView(rv)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = object : ShopListAdapter.OnShopItemClickListener {
            override fun onShopItemClick(shopItem: ShopItem) {
                Log.d(
                    "CHECKER",
                    "ShopItem: id ${shopItem.id} name ${shopItem.name} " +
                            "count ${shopItem.count} state ${shopItem.enabled}"
                )
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onLongShopItemClickListener =
            object : ShopListAdapter.OnLongShopItemClickListener {
                override fun onLongShopItemClick(shopItem: ShopItem) {
                    viewModel.changeEnableState(shopItem)
                }
            }
    }

//    private fun showList(shopList: List<ShopItem>) {      // для реализации через linear layout
//        ll_shopList.removeAllViews()
//        for (shopItem in shopList) {
//            val id: Int = when (shopItem.enabled) {
//                true -> R.layout.item_shop_enabled
//                false -> R.layout.item_shop_disabled
//            }
//            val view = LayoutInflater.from(this).inflate(id, ll_shopList, false)
//            val name = view.findViewById<TextView>(R.id.tv_name)
//            val count = view.findViewById<TextView>(R.id.tv_count)
//            name.text = shopItem.name
//            count.text = shopItem.count.toString()
//            view.setOnLongClickListener {
//                viewModel.changeEnableState(shopItem)
//                true
//            }
//            ll_shopList.addView(view)
//        }
//    }
}
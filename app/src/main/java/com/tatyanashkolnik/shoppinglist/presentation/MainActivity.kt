package com.tatyanashkolnik.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tatyanashkolnik.shoppinglist.R
import com.tatyanashkolnik.shoppinglist.databinding.ActivityMainBinding
import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import com.tatyanashkolnik.shoppinglist.presentation.ShopListAdapter.Companion.MAX_POOL_SIZE

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it) // проделывает вычисления в другом потоке
            // и после устанавливает список в адаптер
        }
        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null) // имя не принципиально | нужно чтобы найти фрагмент
            // добавляет в стек - последовательность нажимания на кнопку назад
            // чтобы удалить с экрана фрагмент
            .commit()
    }

    private fun isOnePaneMode(): Boolean { // режим в одну колонку (вертикальный макет)
        return binding.shopItemContainer == null
    }

    private fun setupRecyclerView() {
        shopListAdapter = ShopListAdapter()
        with(binding.rvShopList) {
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
        setupSwipeListener()
    }

    private fun setupSwipeListener() {
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
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = object : ShopListAdapter.OnShopItemClickListener {
            override fun onShopItemClick(shopItem: ShopItem) {
                if (isOnePaneMode()) {
                    Log.d(
                        "CHECKER",
                        "ShopItem: id ${shopItem.id} name ${shopItem.name} " +
                                "count ${shopItem.count} state ${shopItem.enabled}"
                    )
                    val intent = ShopItemActivity.newIntentEditItem(
                        this@MainActivity, shopItem.id
                    )
                    startActivity(intent)
                } else {
                    launchFragment(ShopItemFragment.newInstanceEditItem(shopItem.id))
                }
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

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
    }
}

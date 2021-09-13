package com.tatyanashkolnik.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tatyanashkolnik.shoppinglist.R
import com.tatyanashkolnik.shoppinglist.databinding.ItemShopDisabledBinding
import com.tatyanashkolnik.shoppinglist.databinding.ItemShopEnabledBinding
import com.tatyanashkolnik.shoppinglist.domain.ShopItem

class ShopListAdapter :
    androidx.recyclerview.widget.ListAdapter<ShopItem, ShopListViewHolder>(
        ShopItemDiffCallback()
    ) {

    var onLongShopItemClickListener: OnLongShopItemClickListener? = null
    var onShopItemClickListener: OnShopItemClickListener? = null

    interface OnLongShopItemClickListener {
        fun onLongShopItemClick(shopItem: ShopItem)
    }

    interface OnShopItemClickListener {
        fun onShopItemClick(shopItem: ShopItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown viewtype $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = holder.binding

        when(binding){
            is ItemShopEnabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
            is ItemShopDisabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
        }
        binding.root.setOnLongClickListener {
            onLongShopItemClickListener?.onLongShopItemClick(shopItem)
            true
        }
        binding.root.setOnClickListener {
            onShopItemClickListener?.onShopItemClick(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val condition = when (item.enabled) {
            true -> VIEW_TYPE_ENABLED
            false -> VIEW_TYPE_DISABLED
        }
        return condition
    }

    override fun getItemCount(): Int = currentList.size // не нужен если ListAdapter

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1
        const val MAX_POOL_SIZE = 10
    }
}
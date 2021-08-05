package com.tatyanashkolnik.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tatyanashkolnik.shoppinglist.R
import com.tatyanashkolnik.shoppinglist.domain.ShopItem

class ShopListAdapter :
    androidx.recyclerview.widget.ListAdapter<ShopItem, ShopListViewHolder>(
        ShopItemDiffCallback()
    ) {

//    var shopList = listOf<ShopItem>()                                                               теперь не нужно хранить ссылку на list
//        set(value) {                                                                                тк listAdapter сам работает со списом
//            val diffCallback = ShopListDiffCallback(shopList, value)
//            val diffResult = DiffUtil.calculateDiff(diffCallback)
//            diffResult.dispatchUpdatesTo(this)
//            field = value
//        }

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

        return ShopListViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.name.text = shopItem.name
        holder.count.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onLongShopItemClickListener?.onLongShopItemClick(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.onShopItemClick(shopItem)
        }
    }


//    override fun getItemViewType(position: Int): Int { /* !!! Если оставить в таком виде, то
//        return super.getItemViewType(position)         ViewType ов будет столько же,
//        return position                                сколько элементов в коллекции и ViewHolder
//                                                        будет каждый раз создаваться заново,
//                                                        тогда зачем нам RecyclerView? :) */
//    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val condition = when (item.enabled) {
            true -> VIEW_TYPE_ENABLED
            false -> VIEW_TYPE_DISABLED
        }
        return condition
    }

    override fun onViewRecycled(holder: ShopListViewHolder) { // тоже не нужен, так как 100 проц
        super.onViewRecycled(holder)                          // устанавливаем новое значение
        holder.name.text = ""
        holder.count.text = ""
    }

    override fun getItemCount(): Int = currentList.size // не нужен если ListAdapter

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1
        const val MAX_POOL_SIZE = 10
    }
}
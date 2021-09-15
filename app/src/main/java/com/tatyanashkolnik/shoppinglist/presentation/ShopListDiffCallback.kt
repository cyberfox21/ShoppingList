package com.tatyanashkolnik.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.tatyanashkolnik.shoppinglist.domain.ShopItem

//Сейчас вместо него используется ShopItemDiffCallback
//он сравнивает не списки, а сами элементы

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

//        val equalId = oldItem.id == newItem.id
//        val equalName = oldItem.name == newItem.name
//        val equalCount = oldItem.count == newItem.count
//        val equalEnabled = oldItem.enabled == newItem.enabled
//        return equalId and equalName and equalCount and equalEnabled

        return oldItem == newItem // так как у датаклассов метод equals сравнивает поля
    }

}
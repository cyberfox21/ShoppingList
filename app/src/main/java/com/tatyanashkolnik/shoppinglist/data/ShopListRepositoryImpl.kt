package com.tatyanashkolnik.shoppinglist.data

import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import com.tatyanashkolnik.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID
import com.tatyanashkolnik.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {

    private var autoIncrementedId = 0

    private val shopList = mutableListOf<ShopItem>()

    init {
        for( i in 0 until 10){
            val item = ShopItem("Name $i", i, true)
            addShopItem(item)
        }
    }

    override fun getShopList(): List<ShopItem> {
        return shopList
    }

    override fun getShopItem(shopItemId: Int): ShopItem? {
        return shopList.find { it.id == shopItemId }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == UNDEFINED_ID) {
            shopItem.id = autoIncrementedId
            autoIncrementedId++
        }
        shopList.add(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        addShopItem(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

}

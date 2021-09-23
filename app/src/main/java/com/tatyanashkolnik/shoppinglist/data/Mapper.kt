package com.tatyanashkolnik.shoppinglist.data

import com.tatyanashkolnik.shoppinglist.domain.ShopItem

class Mapper {

    fun mapEntityToDbModel(shopItem : ShopItem) : ShopItemDbModel{
        return ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enabled = shopItem.enabled
        )
    }

    fun mapDbModelToEntity(shopItemDbModel : ShopItemDbModel) : ShopItem{
        return ShopItem(
            id = shopItemDbModel.id,
            name = shopItemDbModel.name,
            count = shopItemDbModel.count,
            enabled = shopItemDbModel.enabled
        )
    }

    fun mapListDbModelToListEntity(list : List<ShopItemDbModel>) : List<ShopItem>{
        val shopItemList = mutableListOf<ShopItem>()
        for (shopItem in list){
            shopItemList += (mapDbModelToEntity(shopItem))
        }
        return shopItemList
    }
}
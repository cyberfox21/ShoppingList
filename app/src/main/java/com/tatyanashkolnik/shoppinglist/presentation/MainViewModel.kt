package com.tatyanashkolnik.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tatyanashkolnik.shoppinglist.data.ShopListRepositoryImpl
import com.tatyanashkolnik.shoppinglist.domain.DeleteShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.EditShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.GetShopListUseCase
import com.tatyanashkolnik.shoppinglist.domain.ShopItem

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }

}
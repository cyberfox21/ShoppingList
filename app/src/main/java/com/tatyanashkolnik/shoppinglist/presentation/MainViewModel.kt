package com.tatyanashkolnik.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tatyanashkolnik.shoppinglist.data.ShopListRepositoryImpl
import com.tatyanashkolnik.shoppinglist.domain.DeleteShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.EditShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.GetShopListUseCase
import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    // Так как в Room реализованы suspend функции, которые не блокиуют поток, нам нет смысла
    // во ViewModel использовать Dispatchers.IO, поэтому просто будем использовать корутины на Main thread
    val scope = CoroutineScope(Dispatchers.Main) // Не можем взять LifecycleScope как в активити


    fun deleteShopItem(shopItem: ShopItem) {
        scope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        scope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
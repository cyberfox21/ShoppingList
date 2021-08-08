package com.tatyanashkolnik.shoppinglist.presentation

import android.text.TextUtils.isDigitsOnly
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.tatyanashkolnik.shoppinglist.data.ShopListRepositoryImpl
import com.tatyanashkolnik.shoppinglist.domain.AddShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.EditShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.GetShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int): ShopItem? {
        return getShopItemUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if(fieldsValid){
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if(fieldsValid){
            val shopItem = ShopItem(name, count, true)
            editShopItemUseCase.editShopItem(shopItem)
        }
    }

    private fun parseName(name: String?): String{
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?) : Int{
        val result = when{
            count?.isDigitsOnly() == true -> count.toInt() ?: 0
            count?.contains(" ") == true -> count.trim().toInt() ?: 0
            else -> 0
        }
        return result
    }

    private fun validateInput(name: String, count: Int): Boolean{
        var result = true
        if(name.isBlank()){
            // TODO(Show error input name)
            result = false
        }
        if(count <= 0) {
            // TODO(Show error input count)
            result = false
        }
        return result
    }

}
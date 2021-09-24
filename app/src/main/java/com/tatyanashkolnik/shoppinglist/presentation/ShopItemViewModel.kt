package com.tatyanashkolnik.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tatyanashkolnik.shoppinglist.data.ShopListRepositoryImpl
import com.tatyanashkolnik.shoppinglist.domain.AddShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.EditShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.GetShopItemUseCase
import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    private val _shouldCloseScreen = MutableLiveData<Unit>() // тип Unit обозначает то, что нам

    // не важно какой объект мы устанавливаем в LiveData,
    // важно лишь то, что будет оповещение(из-за изменения)
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    // true -> показываем юзеру ошибку | false -> всё ок
    // private _ чтобы устанавливать значения от сюда, из ViewModel
    private val _errorInputName = MutableLiveData<Boolean>()

    // публичная чтобы получить в Activity
    val errorInputName: LiveData<Boolean> // у типа LiveData нельзя устанавливать значение
        get() = _errorInputName// переопр getter чтобы возвращал значение приватной переменной

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    // Так как в Room реализованы suspend функции, которые не блокиуют поток, нам нет смысла
    // во ViewModel использовать Dispatchers.IO, поэтому просто будем использовать корутины на Main thread
    val scope = CoroutineScope(Dispatchers.Main)

    fun getShopItem(shopItemId: Int) {
        scope.launch {
            val item = getShopItemUseCase.getShopItem(shopItemId)
            _shopItem.value = item!!
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            scope.launch {
                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItem.value?.let {
                scope.launch {
                    val item = it.copy(name = name, count = count) // копируем старый объект,
                    // чтобы сохранить его id и состояние enabled, но изменяем отредактированные поля
                    editShopItemUseCase.editShopItem(item)
                    finishWork()
                }
            }
        }
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        val result = try {
            count!!.toInt()
        } catch (e: Exception) {
            0
        }
        return result
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit // сам объект, который прилетит в observer,
        // в активити никак использоваться не будет
    }

    override fun onCleared() { // функция, которая вызывается при смерти ViewModel
        super.onCleared()
        scope.cancel() // отменяем scope
    }
}
package com.tatyanashkolnik.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import com.tatyanashkolnik.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val shopListDao = AppDatabase.getInstance(application).shopItemDao()
    private val mapper = Mapper()

    override fun getShopList(): LiveData<List<ShopItem>> {
        return mapper.mapListDbModelToListEntity(shopListDao.getShopList())
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return mapper.mapDbModelToEntity(shopListDao.getShopItem(shopItemId))
    }

    override fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

}

package com.tatyanashkolnik.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import com.tatyanashkolnik.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val shopListDao = AppDatabase.getInstance(application).shopItemDao()
    private val mapper = Mapper()

    override fun getShopList(): LiveData<List<ShopItem>> =
        MediatorLiveData<List<ShopItem>>().apply { // служит чтобы перехватывать события лайвдаты
            addSource(shopListDao.getShopList()) {
                mapper.mapListDbModelToListEntity(it) // преобразуем один тип лайвдаты в другой
            }
        }
//    Или использовать класс Transformations (под капотом MediatorLiveData)
//    Transformations.map(shopListDao.getShopList()) {
//        mapper.mapListDbModelToListEntity(it)
//    }


    override fun getShopItem(shopItemId: Int): ShopItem =
        mapper.mapDbModelToEntity(shopListDao.getShopItem(shopItemId))


    override fun addShopItem(shopItem: ShopItem) =
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))


    override fun editShopItem(shopItem: ShopItem) =  //такая же реализация как и в addShopItem потому
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem)) // что при добавлении мы
     // указали onConflict REPLACE поэтому это тоже самое, что и редактирование

    override fun deleteShopItem(shopItem: ShopItem) =
        shopListDao.deleteShopItem(shopItem.id)


}

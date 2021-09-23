package com.tatyanashkolnik.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopItemDao {

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItem(shopItemDbModel: ShopItemDbModel)

    @Query("DELETE FROM shop_items WHERE id = :id")
    fun deleteShopItem(id: Int)

    @Query("SELECT * FROM shop_items WHERE id = :id LIMIT 1")
    fun getShopItem(id: Int): ShopItemDbModel


}
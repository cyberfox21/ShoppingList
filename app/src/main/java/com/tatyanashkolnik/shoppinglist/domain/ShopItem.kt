package com.tatyanashkolnik.shoppinglist.domain

data class ShopItem(
    var id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}

package com.tatyanashkolnik.shoppinglist.presentation

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ShopListViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    // ViewDataBinding так как мы не знаем точно, какой layout будет у элемента: активный или нет
    // ViewDataBinding является родительским классом для всех dataBinding объектов
}
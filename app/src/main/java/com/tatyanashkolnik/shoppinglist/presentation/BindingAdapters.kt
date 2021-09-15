package com.tatyanashkolnik.shoppinglist.presentation

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.tatyanashkolnik.shoppinglist.R

interface OnTextChangedListener {
    fun onTextChanged()
}

@BindingAdapter("resetError")
fun resetError(et: EditText, onTextChangedListener: OnTextChangedListener) {
    et.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangedListener.onTextChanged()
        }

        override fun afterTextChanged(s: Editable?) {
        }


    })
}

@BindingAdapter("setTilError")
fun setTilError(til: TextInputLayout, error: Boolean) {
    if (error) {
        val message = when (til.id) {
            R.id.til_name -> til.context.getString(R.string.invalid_name)
            R.id.til_count -> til.context.getString(R.string.invalid_count)
            else -> throw RuntimeException("Unknown id of TextInputLayout ${til.id}")
        }
        til.error = message

    } else {
        til.error = null
    }
}
package com.tatyanashkolnik.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.tatyanashkolnik.shoppinglist.R
import com.tatyanashkolnik.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var btnSave: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    // устанавливать значение извне нельзя, так как при пересоздании все переменные сбрасываются
    private lateinit var onEditingFinishedListener : OnEditingFinishedListener // private lateinit
    // чтобы точно было видно, что необходимо реализовать интерефейс иначе выбрасываем исключение

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

    override fun onAttach(context: Context) { // context - это и есть наша активити
        super.onAttach(context)
        if(context is OnEditingFinishedListener){// проверяем, реализует ли activity нужный listener
            onEditingFinishedListener = context
        } else{
            // если реализация интерфейса необходима, а его отсутствие приводит к багам
            throw RuntimeException("Activity mast implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams() // init vars screenMode and shopItemId which help to launch fragment mode
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        initViews(view)
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.errorInputName.observe(viewLifecycleOwner, { // viewLifecycleOwner, тк view может
            val message = when(it){                     // умереть, а фрагмент остаться жить
                true -> "Invalid name"
                false -> null
            }
            tilName.error = message
        })
        viewModel.errorInputCount.observe(viewLifecycleOwner, {
            val message = when(it){
                true -> "Invalid count"
                false -> null
            }
            tilCount.error = message
        })
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner,{ it ->
            activity?.onBackPressed()
//            requireActivity().onBackPressed() // если уверены что activity не null (крэш возможен)
//            finish()
        })
    }

    private fun launchRightMode(){
        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun launchEditMode(){
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner, { t ->
            etName.setText(t?.name.toString())
            etCount.setText(t.count.toString())
        })
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val count = etCount.text.toString()
            viewModel.editShopItem(name, count)
        }
    }

    private fun launchAddMode(){
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val count = etCount.text.toString()
            viewModel.addShopItem(name, count)
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        btnSave = view.findViewById(R.id.btn_save)
    }

    private fun parseParams() {
        val args = requireArguments()
        if(!args.containsKey(SCREEN_MODE)){
            throw java.lang.RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if(mode != MODE_ADD && mode != MODE_EDIT){
            throw java.lang.RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if(screenMode == MODE_EDIT){
            if(!args.containsKey(SHOP_ITEM_ID)) {
                throw java.lang.RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object{

        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply{
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }
}
package com.example.moneymanagement.presentation.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<VB : ViewBinding>(private val bindingFactory: (LayoutInflater) -> VB) :
    AppCompatActivity(), BaseView {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = bindingFactory(layoutInflater)
        setContentView(_binding?.root)

        initializeComponent()
        initializeEvents()
        initializeData()
        bindView()
    }


    override fun initializeComponent() {
        window.decorView.setSystemUiVisibility(
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        )
    }

    override fun initializeEvents() {
    }

    override fun initializeData() {
    }

    override fun bindView() {
    }

}
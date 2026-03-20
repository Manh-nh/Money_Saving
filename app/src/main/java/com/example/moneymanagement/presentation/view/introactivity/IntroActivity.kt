package com.example.moneymanagement.presentation.view.introactivity

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.moneymanagement.databinding.ActivityIntroBinding
import com.example.moneymanagement.presentation.database.model.IntroModel
import com.example.moneymanagement.presentation.view.adapter.IntroAdapter
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.example.moneymanagement.presentation.view.homeactivity.HomeActivity

class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {


    private lateinit var adapter: IntroAdapter
    private lateinit var viewModel: IntroViewModel
    private var data: ArrayList<IntroModel> = ArrayList()


    override fun initializeComponent() {
        super.initializeComponent()

        viewModel = ViewModelProvider(this)[IntroViewModel::class.java]

        data = viewModel.initData()
        adapter = IntroAdapter(data)
        binding.viewPagerIntro.adapter = adapter

        binding.dotsIndicator.attachTo(binding.viewPagerIntro)

    }


    override fun initializeEvents() {
        binding.btnNext.setOnClickListener { nextIntro() }


        binding.viewPagerIntro.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    binding.viewPagerIntro.currentItem = position
                    if (position == adapter.itemCount - 1) {
                        binding.btnNext.text = "start"
                    } else {
                        binding.btnNext.text = "next"
                    }

                }


            }
        )

    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun bindView() {
        super.bindView()
    }

    private fun nextIntro() {
        if (binding.viewPagerIntro.currentItem == adapter.itemCount - 1) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent);
            finish();
        } else {
            val currentItem = binding.viewPagerIntro.currentItem
            binding.viewPagerIntro.currentItem = currentItem + 1
        }
    }


}
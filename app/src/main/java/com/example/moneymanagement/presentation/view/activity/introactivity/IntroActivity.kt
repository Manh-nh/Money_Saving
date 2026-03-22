package com.example.moneymanagement.presentation.view.activity.introactivity

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.ActivityIntroBinding
import com.example.moneymanagement.presentation.database.model.IntroModel
import com.example.moneymanagement.presentation.view.adapter.IntroAdapter
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.example.moneymanagement.presentation.view.activity.homeactivity.HomeActivity

class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {


    private lateinit var adapter: IntroAdapter
    private var data: ArrayList<IntroModel> = ArrayList()


    override fun initializeComponent() {
        super.initializeComponent()


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
                        binding.btnNext.text = this@IntroActivity.getText(R.string.start)
                    } else {
                        binding.btnNext.text = this@IntroActivity.getText(R.string.next)
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


    fun initData(): ArrayList<IntroModel> {

        data.add(
            IntroModel(
                R.drawable.intro1,
                this.getString(R.string.title1),
                this.getString(R.string.content1)

            )
        )
        data.add(
            IntroModel(
                R.drawable.intro2,
                this.getString(R.string.title2),
                this.getString(R.string.content2)

            )
        )
        data.add(
            IntroModel(
                R.drawable.intro3,
                this.getString(R.string.title3),
                this.getString(R.string.content3)
            )
        )
        data.add(
            IntroModel(
                R.drawable.intro4,
                this.getString(R.string.title4),
                this.getString(R.string.content4)
            )
        )
        return data
    }


}
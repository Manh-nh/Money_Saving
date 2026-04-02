package com.example.moneymanagement.presentation.view.activity.splash

import android.content.Intent
import android.content.SharedPreferences
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.ActivitySplashBinding
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.example.moneymanagement.presentation.view.activity.home.HomeActivity
import com.example.moneymanagement.presentation.view.activity.intro.IntroActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import androidx.core.content.edit

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun bindView() {

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        Glide.with(this).load(R.drawable.img_title).into(binding.imgLogo)
        Glide.with(this).load(R.drawable.img_start).into(binding.imgLogoSmall)

        moveToView()

    }

    private fun moveToView() {

        val sharedPreferences: SharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE)
        val isFirstRun: Boolean = sharedPreferences.getBoolean("isFirstRun", true)

        CoroutineScope(Dispatchers.IO).launch {
            sleep(4000)

            if (isFirstRun) {
                val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                startActivity(intent)
                sharedPreferences.edit { putBoolean("isFirstRun", false) }
            } else {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            finish()
        }

    }

}
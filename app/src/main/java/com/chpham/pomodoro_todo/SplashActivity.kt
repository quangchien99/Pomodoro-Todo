package com.chpham.pomodoro_todo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.viewbinding.ViewBinding
import com.chpham.pomodoro_todo.base.ui.BaseActivity
import com.chpham.pomodoro_todo.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    companion object {
        const val ANIMATION_TIME: Long = 2000L
    }

    override fun initViewBinding(): ViewBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(this.mainLooper).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, ANIMATION_TIME)
    }
}

package com.hjw.autoscript.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hjw.accessibilitylib.ServiceHelper
import com.hjw.autoscript.MyAccessibilityService
import com.hjw.autoscript.R
import com.hjw.autoscript.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.apply {
            viewModel = mViewModel

            startService.setOnClickListener {
                ServiceHelper.jumpAccessibilityPage(this@MainActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        checkService()
    }

    private fun checkService() {
        val isServiceOn = if (ServiceHelper.isServiceOn(
                this@MainActivity,
                MyAccessibilityService::class.java.name
            )
        ) "是" else "否"
        mBinding.alreadyStartService.text = "是否已经启用无障碍服务：${isServiceOn}"
    }
}
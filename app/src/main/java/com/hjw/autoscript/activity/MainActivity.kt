package com.hjw.autoscript.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.ToastUtils
import com.hjw.accessibilitylib.AccessibilityHelper
import com.hjw.accessibilitylib.ServiceHelper
import com.hjw.accessibilitylib.util.AccessibilityServiceUtil
import com.hjw.accessibilitylib.service.MyAccessibilityService
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
                AccessibilityServiceUtil.jumpAccessibilityPage(this@MainActivity)
            }

            // 启动截屏服务，首先申请权限
            startScript.setOnClickListener {
                if (AccessibilityServiceUtil.isServiceOn(
                        this@MainActivity,
                        MyAccessibilityService::class.java.name
                    )
                ) {
                    AccessibilityHelper.startCaptureIntent(
                        this@MainActivity,
                        REQUEST_CODE_MEDIA_PROJECTION
                    )
                } else {
                    ToastUtils.showShort("请先开始无障碍服务")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        checkService()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 启动截屏服务
        if (requestCode == REQUEST_CODE_MEDIA_PROJECTION && resultCode == RESULT_OK) {
            ServiceHelper.initCaptureService(this, resultCode, data)
        }
    }

    /**
     * 检查无障碍服务
     */
    private fun checkService() {
        val isServiceOn = if (AccessibilityServiceUtil.isServiceOn(
                this@MainActivity,
                MyAccessibilityService::class.java.name
            )
        ) "是" else "否"
        mBinding.alreadyStartService.text = "是否已经启用无障碍服务：${isServiceOn}"
    }

    companion object {
        const val REQUEST_CODE_MEDIA_PROJECTION = 1001
    }
}
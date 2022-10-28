package com.hjw.autoscript.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.hjw.accessibilitylib.AccessibilityHelper
import com.hjw.accessibilitylib.ServiceHelper
import com.hjw.autoscript.MyAccessibilityService
import com.hjw.autoscript.R
import com.hjw.autoscript.databinding.ActivityMainBinding
import com.hjw.autoscript.service.ScreenCaptureService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel by viewModels<MainViewModel>()

    private var screenCaptureService: ScreenCaptureService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            LogUtils.i("onServiceConnected!!!")
            screenCaptureService = (service as ScreenCaptureService.MyBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            LogUtils.i("onServiceDisconnected!!!")
            screenCaptureService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.apply {
            viewModel = mViewModel

            startService.setOnClickListener {
                ServiceHelper.jumpAccessibilityPage(this@MainActivity)
            }

            // 启动截屏服务，首先申请权限
            startScript.setOnClickListener {
                AccessibilityHelper.startCaptureIntent(
                    this@MainActivity,
                    REQUEST_CODE_MEDIA_PROJECTION
                )

                lifecycleScope.launch(Dispatchers.Main) {
                    delay(5000)
                    val bitmap = screenCaptureService?.startCapture()
                    LogUtils.i("bitmap=${bitmap?.byteCount}")
                    screenshotView.setImageBitmap(bitmap)


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
            bindService(Intent(this, ScreenCaptureService::class.java).apply {
                putExtra(ScreenCaptureService.INTENT_CODE, resultCode)
                putExtra(ScreenCaptureService.INTENT_DATA, data)
            }, serviceConnection, BIND_AUTO_CREATE)
        }
    }

    /**
     * 检查无障碍服务
     */
    private fun checkService() {
        val isServiceOn = if (ServiceHelper.isServiceOn(
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
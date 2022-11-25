package com.hjw.autoscript.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjw.accessibilitylib.service.MyAccessibilityService
import com.hjw.accessibilitylib.util.AccessibilityServiceUtil
import com.hjw.autoscript.R
import com.hjw.autoscript.databinding.ActivityMainBinding
import com.hjw.autoscript.service.StartScriptService
import com.hjw.autoscript.utils.PreferenceUtils
import com.hjw.floatview.FloatViewManager
import com.hjw.gamelogic.GameLogicController
import com.hjw.screencapture.ScreenCaptureHelper
import com.hjw.screencapture.service.ScreenCaptureService

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel by viewModels<MainViewModel>()

    private var startScriptService: StartScriptService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            LogUtils.i("onServiceConnected!!!")
            startScriptService = (service as StartScriptService.MyBinder).getService()

            startScriptService?.apply {
                // 配置菜单功能
                saveDanrenPlan(mBinding.planDanren.isChecked)

                // 开始脚本
                startScript()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            LogUtils.i("onServiceDisconnected!!!")
            startScriptService = null
        }
    }

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
                if (startScript.text.contains("脚本执行中")) {
                    return@setOnClickListener
                }

                if (AccessibilityServiceUtil.isServiceOn(
                        this@MainActivity,
                        MyAccessibilityService::class.java.name
                    )
                ) {
                    ScreenCaptureHelper.startCaptureIntent(
                        this@MainActivity,
                        REQUEST_CODE_MEDIA_PROJECTION
                    )

                    // 启动脚本服务
                    startScriptService()
                } else {
                    ToastUtils.showShort("请先开始无障碍服务")
                }
            }

            // 点击单人玩法后，默认全部选中
            planDanren.setOnCheckedChangeListener { buttonView, isChecked ->
                itemShimen.isChecked = isChecked
                itemBaotu.isChecked = isChecked
                itemFengyao.isChecked = isChecked
                itemChumo.isChecked = isChecked
                itemYunbiao.isChecked = isChecked
                itemBangpaiDaily.isChecked = isChecked
                itemPaoshang.isChecked = isChecked
                itemXiulian.isChecked = isChecked
                itemQiyuan.isChecked = isChecked
                itemLeitai.isChecked = isChecked
                itemMijing.isChecked = isChecked
                itemGuiwang.isChecked = isChecked
                itemWabaotu.isChecked = isChecked
            }
            // 获取已保存的单人玩法状态
            planDanren.isChecked = PreferenceUtils.getDanrenPlanStatus()

            // 单机一条龙后，默认全部选中
            planOneDragon.setOnCheckedChangeListener { buttonView, isChecked ->
                oneDragonItemFengyao.isChecked = isChecked
                oneDragonItemZhuogui.isChecked = isChecked
                oneDragonItemFuben50.isChecked = isChecked
                oneDragonItemFuben50Jingying.isChecked = isChecked
//                oneDragonItemFuben75.isChecked = isChecked
//                oneDragonItemFuben75Jingying.isChecked = isChecked
//                oneDragonItemFuben100.isChecked = isChecked
//                oneDragonItemFuben100Jingying.isChecked = isChecked
            }
        }
    }

    override fun onResume() {
        super.onResume()

        checkService()

        if (GameLogicController.isScriptStarting()) {
            mBinding.startScript.text = "脚本执行中..."
        } else {
            mBinding.startScript.text = "开始执行脚本"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 启动截屏服务
        if (requestCode == REQUEST_CODE_MEDIA_PROJECTION && resultCode == RESULT_OK) {
            ScreenCaptureHelper.initCaptureService(this, resultCode, data)

            mBinding.startScript.text = "脚本执行中..."

//            val manager = FloatViewManager()
//            manager.setItemListener(object : FloatViewManager.OnItemListener {
//                override fun onPauseScript() {
//
//                }
//
//                override fun onStartScript() {
//
//                }
//
//                override fun onStopScript() {
//
//                }
//            })
//            manager.showWindow(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ScreenCaptureHelper.stopCaptureService(this)
        stopScriptService()
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

    private fun startScriptService() {
        bindService(
            Intent(this, StartScriptService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun stopScriptService() {
        unbindService(serviceConnection)
    }

    companion object {
        const val REQUEST_CODE_MEDIA_PROJECTION = 1001
    }
}
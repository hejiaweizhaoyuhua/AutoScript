package com.hjw.autoscript.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.ToastUtils
import com.hjw.accessibilitylib.service.MyAccessibilityService
import com.hjw.accessibilitylib.util.AccessibilityServiceUtil
import com.hjw.autoscript.R
import com.hjw.autoscript.databinding.ActivityMainBinding
import com.hjw.autoscript.utils.PreferenceUtils
import com.hjw.gamelogic.GameLogicController
import com.hjw.screencapture.ScreenCaptureHelper

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

                    mViewModel.saveDanrenPlan(planDanren.isChecked)
                    mViewModel.startScript()
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
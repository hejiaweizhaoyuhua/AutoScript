package com.hjw.autoscript.utils

import com.blankj.utilcode.util.SPUtils

object PreferenceUtils {
    const val PREFERENCE_USER_CONFIG = "user_config"

    const val IS_DANREN_PLAN = "is_danren_plan"
    const val DANREN_SHIMEN = "danren_shimen"
    const val DANREN_BAOTU = "danren_baotu"
    const val DANREN_FENGYAO = "danren_fengyao"
    const val DANREN_CHUMO = "danren_chumo"
    const val DANREN_YUNBIAO = "danren_yunbiao"
    const val DANREN_BANGPAI_DAILY = "danren_bangpai_daily"
    const val DANREN_PAOSHANG = "danren_paoshang"
    const val DANREN_XIULIAN = "danren_xiulian"
    const val DANREN_QIYUAN = "danren_qiyuan"
    const val DANREN_LEITAI = "danren_leitai"
    const val DANREN_MIJING = "danren_mijing"
    const val DANREN_GUIWANG = "danren_guiwang"
    const val DANREN_WABAOTU = "danren_wabaotu"

    const val ONE_DRAGON_FENGYAO = "one_dragon_fengyao"
    const val ONE_DRAGON_CHUMO = "one_dragon_chumo"
    const val ONE_DRAGON_50FUBEN = "one_dragon_50fuben"
    const val ONE_DRAGON_50FUBEN_JINGYING = "one_dragon_50fuben_jingying"
    const val ONE_DRAGON_75FUBEN = "one_dragon_75fuben"
    const val ONE_DRAGON_75FUBEN_JINGYING = "one_dragon_75fuben_jingying"
    const val ONE_DRAGON_100FUBEN = "one_dragon_75fuben"
    const val ONE_DRAGON_100FUBEN_JINGYING = "one_dragon_100fuben_jingying"

    fun saveDanrenPlan(isEnable: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(IS_DANREN_PLAN, isEnable)
    }

    fun getDanrenPlanStatus(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(IS_DANREN_PLAN)
    }

    /** 师门 **/
    fun shimen(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_SHIMEN, isFinish)
    }
    fun isFinishShimen(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_SHIMEN)
    }

    /** 宝图 **/
    fun baotu(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_BAOTU, isFinish)
    }
    fun isFinishBaotu(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_BAOTU)
    }

    /** 封妖 **/
    fun fengyao(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_FENGYAO, isFinish)
    }
    fun isFinishFengyao(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_FENGYAO)
    }

    /** 除魔 **/
    fun chumo(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_CHUMO, isFinish)
    }
    fun isFinishChumo(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_CHUMO)
    }

    /** 运镖 **/
    fun yunbiao(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_YUNBIAO, isFinish)
    }
    fun isFinishYunbiao(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_YUNBIAO)
    }

    /** 帮派日常 **/
    fun bangpaiDaily(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_BANGPAI_DAILY, isFinish)
    }
    fun isFinishBangpaiDaily(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_BANGPAI_DAILY)
    }

    /** 跑商 **/
    fun paoshang(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_PAOSHANG, isFinish)
    }
    fun isFinishPaoshang(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_PAOSHANG)
    }

    /** 修炼 **/
    fun xiulian(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_XIULIAN, isFinish)
    }
    fun isFinishXiulian(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_XIULIAN)
    }

    /** 祈愿 **/
    fun qiyuan(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_QIYUAN, isFinish)
    }
    fun isFinishQiyuan(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_QIYUAN)
    }

    /** 擂台 **/
    fun leitai(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_LEITAI, isFinish)
    }
    fun isFinishLeitai(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_LEITAI)
    }

    /** 蜃龙迷境 **/
    fun mijing(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_MIJING, isFinish)
    }
    fun isFinishMijing(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_MIJING)
    }

    /** 鬼王 **/
    fun guiwang(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_GUIWANG, isFinish)
    }
    fun isFinishGuiwang(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_GUIWANG)
    }

    /** 挖宝图 **/
    fun wabaotu(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(DANREN_WABAOTU, isFinish)
    }
    fun isFinishWabaotu(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(DANREN_WABAOTU)
    }

    /** 一条龙封妖 **/
    fun dragonFengyao(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_FENGYAO, isFinish)
    }
    fun isFinishDragonFengyao(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_FENGYAO)
    }

    /** 一条龙除魔 **/
    fun dragonChumo(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_CHUMO, isFinish)
    }
    fun isFinishDragonChumo(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_CHUMO)
    }

    /** 50副本 **/
    fun fuben50(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_50FUBEN, isFinish)
    }
    fun isFinishFuben50(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_50FUBEN)
    }

    /** 50精英副本 **/
    fun jingyingFuben50(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_50FUBEN_JINGYING, isFinish)
    }
    fun isFinishJingyingFuben50(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_50FUBEN_JINGYING)
    }

    /** 75副本 **/
    fun fuben75(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_75FUBEN, isFinish)
    }
    fun isFinishFuben75(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_75FUBEN)
    }

    /** 75精英副本 **/
    fun jingyingFuben75(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_75FUBEN_JINGYING, isFinish)
    }
    fun isFinishJingyingFuben75(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_75FUBEN_JINGYING)
    }

    /** 100副本 **/
    fun fuben100(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_100FUBEN, isFinish)
    }
    fun isFinishFuben100(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_100FUBEN)
    }

    /** 100精英副本 **/
    fun jingyingFuben100(isFinish: Boolean) {
        SPUtils.getInstance(PREFERENCE_USER_CONFIG).put(ONE_DRAGON_100FUBEN_JINGYING, isFinish)
    }
    fun isFinishJingyingFuben100(): Boolean {
        return SPUtils.getInstance(PREFERENCE_USER_CONFIG).getBoolean(ONE_DRAGON_100FUBEN_JINGYING)
    }
}
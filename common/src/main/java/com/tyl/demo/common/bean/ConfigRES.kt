package com.tyl.demo.common.bean

import android.os.Parcelable
import com.stx.xhb.androidx.entity.SimpleBannerInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfigRES(
    var common_config: CommonConfig? = null, // 通用配置
    var gift_config_type: ArrayList<GiftBean>? = null,  // 道具配置
    var pic_check_mark_config: PicCheckMarkConfigBean? = null, // 图片检测结果 显示刻度配置 (人脸识别阈值)
    var interests_tags_config: ArrayList<InterestsTag>? = null, // 兴趣爱好配置
    var share_avatarbg_config: ArrayList<String>? = null, // 用户形象分享的边框
    var home_effect_config: ArrayList<HomeEffectConfig>? = null, // 首页语音提示配置
    var invite_new_config: InviteBannerConfigRES? = null // 邀请奖励活动配置
) : Parcelable {

    object ConfigTypes {
        const val COMMON_CONFIG = "common_config"
        const val GIFT_CONFIG = "gift_config_type"
        const val PIC_CHECK_MARK_CONFIG = "pic_check_mark_config"
        const val INTERESTS_TAGS_CONFIG = "interests_tags_config"
        const val SHARE_AVATAR_BG_CONFIG = "share_avatarbg_config"
        const val HOME_EFFECT_CONFIG = "home_effect_config"
        const val INVITE_NEW_CONFIG = "invite_new_config"

        val ALL_CONFIG = arrayListOf(
            COMMON_CONFIG,
            GIFT_CONFIG,
            PIC_CHECK_MARK_CONFIG,
            INTERESTS_TAGS_CONFIG,
            SHARE_AVATAR_BG_CONFIG,
            HOME_EFFECT_CONFIG,
            INVITE_NEW_CONFIG
        )
    }

}

operator fun ConfigRES?.plus(info: ConfigRES?): ConfigRES? {
    if (this == null && info == null) return null
    // 字段修改后需要在这里补充
    return ConfigRES(
        common_config = info?.common_config ?: this?.common_config,
        gift_config_type = info?.gift_config_type ?: this?.gift_config_type,
        pic_check_mark_config = info?.pic_check_mark_config ?: this?.pic_check_mark_config,
        interests_tags_config = info?.interests_tags_config ?: this?.interests_tags_config,
        share_avatarbg_config = info?.share_avatarbg_config ?: this?.share_avatarbg_config,
        home_effect_config = info?.home_effect_config ?: this?.home_effect_config,
        invite_new_config = info?.invite_new_config ?: this?.invite_new_config
    )
}

@Parcelize
data class CommonConfig(
    val zego_app_sign: String?,
    val sys_customer_service_id: String?, // MM小助手（客服）ID
    val new_icon_ability: Int? = null, // 1可定制个性形象，其他不可定制；
    val new_icon_coins: Int? = null, // 定制个性形象麦浪价格
    val member_describe_max_word_count: Int? = null, // 个人简介最大字数
    val Interest_max_count: Int? = null, // 兴趣爱好最大数量
    val alert_follow_win_time: Int? = null, // 提示关注弹窗(秒)
    val alert_follow_tips_time: Int? = null, // 提示关注气泡(秒)
    val show_follow_recommend: Int? = 0, // 登录时推荐关注 0关1开
    val room_user_delay_time: Long? = 0 // 用户进房后延迟展示时间
) : Parcelable

@Parcelize
data class GiftBean(
    val giftId: String? = "",
    val name: String? = "",
    val gif: String? = "", // 道具动图
    val icon: String? = "", // 道具图标
    val scene: Int = 0,  // 礼物类型1私聊2房间保留字段
    val gold: Int = 0,  // 价格 麦子
    val type: Int = 0,  //礼物类型 10私聊礼物 20房间普通礼物 21房间上麦专用礼物 30超级喜欢
    val description: String? = "", // 描述
    val animation: String? = "", //动效
    val bgm: String? = "", // 音效
    val comboTime: Int = 0, // 连击数量
    val perSendNum: Int = 0, // 每次发送数量
    val perComboNum: Int = 0, // 每次连击数量
    val comboGif: String? = "" // 连击时的动图（鼓掌的猴子）
) : Cloneable, Parcelable {
    public override fun clone(): GiftBean {
        return GiftBean(giftId, name, gif, icon, gold, scene, type, description, animation)
    }
}

/**
 * 图片结果刻度配置
 */
@Parcelize
data class PicCheckMarkConfigBean(
    val sharpnessMax: Int = 0,//清晰度
    val sharpness: ArrayList<MarkBean> = ArrayList(),
    val aestheticsMax: Int = 0,//美观度
    val aesthetics: ArrayList<MarkBean> = ArrayList(),
    val ageMax: Int = 0,//年龄
    val age: ArrayList<MarkBean> = ArrayList()
) : Parcelable

@Parcelize
data class MarkBean(
    val code: Int = 0,
    val message: String = ""
) : Parcelable

/**
 * 兴趣爱好bean
 */
@Parcelize
data class InterestsTag(
    val id: Long? = 0,
    val createTime: String? = null,
    val updateTime: String? = null,
    val deleted: Int? = null,
    val interestId: Int? = null,//兴趣id
    val interestName: String? = null//兴趣名称
) : Parcelable

@Parcelize
data class HomeEffectConfig(
    val type: Int = -1,
    val text: String? = "",
    val femaleVoiceUrl: String? = "",
    val maleVoiceUrl: String? = "",
    val showCount: Int = -1,
    val expireTime: Long = 0L
) : Parcelable

@Parcelize
data class InviteBannerConfigRES(
    val banner: ArrayList<InviteBannerConfigItemRES>? = null,
    val status: Int = 0// 开关: 0关闭 1开启(默认)
) : Parcelable

@Parcelize
data class InviteBannerConfigItemRES(
    val image: String = "",
    val url: String = ""
) : SimpleBannerInfo(), Parcelable {
    override fun getXBannerUrl(): Any {
        return url
    }
}
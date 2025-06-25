package com.xut

import com.xut.domain.model.Meaning
import com.xut.domain.model.Region

object Constants {
    const val GITHUB_SOURCE_CODE_URL = "https://github.com/RohitVerma882/XUnlockTool"

    const val LOGIN_URL =
        "https://account.xiaomi.com/pass/serviceLogin?sid=unlockApi&json=false&passive=true&hidden=false&_snsDefault=facebook&checkSafePhone=true&_locale=en"
    const val LOGIN_URL2 =
        "https://account.xiaomi.com/pass/serviceLogin?sid=unlockApi&_json=true&passive=true&hidden=false"

    const val SID = "miui_unlocktool_client"
    const val CLIENT_VERSION = "5.5.224.55"
    const val NONCEV2 = "/api/v2/nonce"
    const val USERINFOV3 = "/api/v3/unlock/userinfo"
    const val DEVICECLEARV3 = "/api/v2/unlock/device/clear"
    const val AHAUNLOCKV3 = "/api/v3/ahaUnlock"

    const val UNLOCK_HMAC_KEY = "2tBeoEyJTunmWUGq7bQH2Abn0k2NhhurOaqBfyxCuLVgn4AVj7swcawe53uDUno"
    const val DEFAULT_IV = "0102030405060708"

    val regions = listOf(
        Region(R.string.region_global, "unlock.update.intl.miui.com"),
        Region(R.string.region_india, "in-unlock.update.intl.miui.com"),
        Region(R.string.region_russia, "ru-unlock.update.intl.miui.com"),
        Region(R.string.region_europe, "eu-unlock.update.intl.miui.com"),
        Region(R.string.region_china, "unlock.update.miui.com")
    )

    val meanings = listOf(
        Meaning(10000, R.string.meaning_10000),
        Meaning(10001, R.string.meaning_10001),
        Meaning(10002, R.string.meaning_10002),
        Meaning(10003, R.string.meaning_10003),
        Meaning(10004, R.string.meaning_10004),
        Meaning(10005, R.string.meaning_10005),
        Meaning(10006, R.string.meaning_10006),
        Meaning(20030, R.string.meaning_10030),
        Meaning(20031, R.string.meaning_10031),
        Meaning(20032, R.string.meaning_10032),
        Meaning(20033, R.string.meaning_10033),
        Meaning(20034, R.string.meaning_10034),
        Meaning(20035, R.string.meaning_10035),
        Meaning(20036, R.string.meaning_10036),
        Meaning(20037, R.string.meaning_10037),
        Meaning(20041, R.string.meaning_10041)
    )
}
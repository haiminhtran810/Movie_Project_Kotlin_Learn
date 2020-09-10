package learn.htm.projectlearn.data

object Constants {
    const val DATABASE_NAME = "movie.db"
    const val VERSION_DB = 1

    const val APP_WEB_VIEW = " APP_WEB_VIEW"
    const val PREF_FIREBASE_TOKEN = "firebase_token"

    const val DEFAULT_FIRST_PAGE = 1
    const val DEFAULT_NUM_VISIBLE_THRESHOLD = 3
    const val DEFAULT_ITEM_PER_PAGE = 10
    const val THRESHOLD_CLICK_TIME = 1000
    const val MIN_PASSWORD_LENGTH = 8

    const val ANDROID_OS = "ANDROID"
    const val USER_AGENT = "User-Agent"
    const val USER_AGENT_VALUE = "abc/okhttp/3.12.0"

    // Format only for data and domain layer, not for presentation layer.
    // If you want use for presentation layer, find format in configs.xml file
    // because we might make app with various language.
    const val FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    const val FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_YYYY_MM_DD_SLASH = "yyyy/MM/dd"
    const val FORMAT_YYYY_MM_DD_JP = "yyyy年MM月dd日"
    const val FORMAT_MM_DD_JP = "MM月dd日"
    const val FORMAT_HH_MM = "HH:mm"
    const val FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd\'T\'HH:mm:ss.SSSS"
    const val FORMAT_YYYY_MM_DD_HH_MM_SS_Z = "yyyy-MM-dd HH:mm:ss Z"
    const val FORMAT_YYYY_MM_DD_DOT = "yyyy.MM.dd"
    const val FORMAT_YYYY_M_DOT = "yyyy.M"
    const val FORMAT_YYYY_M_D_DOT = "yyyy.M.d"
    const val FORMAT_YYYY_M_DD_DOT = "yyyy.M.dd"
    const val FORMAT_YYYY_M_D = "yyyy-M-d"
    const val FORMAT_YYYYMMDD = "yyyyMMdd"
    const val FORMAT_YYYYMM = "yyyyMM"
    const val FORMAT_YYYY = "yyyy"
    const val FORMAT_MM = "MM"
    const val FORMAT_MMMM = "MMMM"
    const val FORMAT_DD_MM_YYYY = "dd-MM-yyyy"
    const val FORMAT_DD_MM_YYYY_SLASH = "dd/MM/yyyy"
    const val FORMAT_HH_MM_SS = "HH:mm:ss"
    const val FORMAT_M_DD_EEE_DOT = "M.dd.EEE"
    const val FORMAT_DD = "dd"
    const val FORMAT_HHMM = "HHmm"

    const val FORCE_UPDATE_APP_CODE = 426
    const val REFRESH_TOKEN_EXPIRED = 901
}

object HttpClient {
    const val CONNECT_TIMEOUT = 10L
    const val READ_TIMEOUT = 10L
    const val WRITE_TIMEOUT = 10L
    const val CONNECTION_TIME_OUT_MLS = CONNECT_TIMEOUT * 1000L
}

object Authentication {
    const val MAX_RETRY = 1
    const val X_OS_TYPE_PARAM = "X-OS-TYPE"
    const val X_APP_VERSION_PARAM = "X-APP-VERSION"
    const val X_APP_BUILD_PARAM = "X-APP-BUILD"
    const val X_FIVE_G_PROMOTION_ACCESS_TOKEN = "X-ACCESSTOKEN"
    const val CHECK_VERSION_STATUS_OK = "OK"
}
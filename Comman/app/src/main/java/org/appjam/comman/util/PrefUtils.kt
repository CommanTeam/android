package org.appjam.comman.util

import android.content.Context
import com.kakao.usermgmt.response.model.UserProfile

/**
 * Created by junhoe on 2017. 12. 31..
 */
object PrefUtils {

    const val FILE_NAME = "CommanPrefFile"
    const val USER_TOKEN = "userToken"
    const val USER_THUMBNAIL = "userThumbnail"
    fun getUserToken(context: Context): String {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val userToken = pref.getString(USER_TOKEN, "")
        if (userToken == "") {
            throw RuntimeException("user token is null. Please check login process")
        }
        return userToken
    }

    fun putUserToken(context: Context, token: String) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
        editor.commit()
    }

    fun putUserProfile(context: Context, userProfile: UserProfile) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(USER_THUMBNAIL, userProfile.thumbnailImagePath)
        editor.apply()
        editor.commit()
    }

    fun getString(context: Context, key: String) : String {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return pref.getString(key, "")
    }
}
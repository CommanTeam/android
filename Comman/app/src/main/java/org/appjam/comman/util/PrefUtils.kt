package org.appjam.comman.util

import android.content.Context
import com.google.android.youtube.player.YouTubePlayer

/**
 * Created by junhoe on 2017. 12. 31..
 */
object PrefUtils {

    const val FILE_NAME = "CommanPrefFile"
    const val USER_TOKEN = "userToken"
    const val LECTURE_ID = "lectureID"
    const val POSITION = "currentPoistion"
    const val CURRENT_TIME = "youtubeCurrentTime"
    const val DURATION_TIME = "youtubeDurationTime"

    fun getUserToken(context: Context): String {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val userToken = pref.getString(USER_TOKEN, "")
<<<<<<< HEAD
//        if (userToken == "") {
//            throw RuntimeException("user token is null. Please check login process")
//        }
=======
>>>>>>> master
        return userToken
    }

    fun putUserToken(context: Context, token: String) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
        editor.commit()
    }

    fun putCurrentLectureID(context: Context, lectureID: Int) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(LECTURE_ID, lectureID)
        editor.apply()
        editor.commit()
    }

    fun putCurrentLecturePosition(context: Context, position: Int) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(POSITION, position)
        editor.apply()
        editor.commit()
    }

    fun putLectureOfCourseID(context: Context, lectureID: Int, courseID: Int) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(courseID.toString(), lectureID)
        editor.apply()
        editor.commit()
    }

    fun putLectureOfCoursePosition(context: Context, position: Int, courseID: Int) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(courseID.toString(), position)
        editor.apply()
        editor.commit()
    }

    fun getRecentLectureOfCourseID(context: Context, courseID: Int) : Int {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return pref.getInt(courseID.toString(), -1)
    }

    fun getRecentLectureOfCoursePosition(context: Context, courseID: Int) : Int {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return pref.getInt(courseID.toString(), -1)
    }

    fun putYoutubeCurrentTime(context: Context, youtubePlayer: YouTubePlayer) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(CURRENT_TIME, youtubePlayer.currentTimeMillis)
        editor.apply()
        editor.commit()
    }

    fun putYoutubeDurationTime(context: Context, youtubePlayer: YouTubePlayer) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(DURATION_TIME, youtubePlayer.durationMillis)
        editor.apply()
        editor.commit()
    }

    fun getString(context: Context, key: String) : String {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return pref.getString(key, "")
    }

    fun getInt(context: Context, key: String) : Int {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }

}
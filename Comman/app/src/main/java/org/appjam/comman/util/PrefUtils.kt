package org.appjam.comman.util

import android.content.Context
import com.google.android.youtube.player.YouTubePlayer
import com.google.gson.Gson
import org.appjam.comman.network.data.LoginData
import org.appjam.comman.network.data.QuizData

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
    const val ANSWERS = "AnswerArray"
    const val NICKNAME = "nickname"
    const val USER_IMAGE = "user_image"

    fun getUserToken(context: Context): String {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val userToken = pref.getString(USER_TOKEN, "")
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
        editor.putInt(courseID.toString() + "position", position)
        editor.apply()
        editor.commit()
    }

    fun getRecentLectureOfCourseID(context: Context, courseID: Int) : Int {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return pref.getInt(courseID.toString(), -1)
    }

    fun getRecentLectureOfCoursePosition(context: Context, courseID: Int) : Int {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return pref.getInt(courseID.toString() + "position", 0)
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

    fun putAnswerArr(context: Context, ansArr: QuizData.AnswerArr) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val editor = pref.edit()
        editor.putString(ANSWERS, gson.toJson(ansArr))
        editor.apply()
        editor.commit()
    }

    fun putUserInfo(context: Context, userInfo: LoginData.UserData) {
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(NICKNAME, userInfo.nickname)
        editor.putString(USER_IMAGE, userInfo.thumbnail_image)
        editor.apply()
        editor.commit()
    }

    fun getAnswerArr(context: Context, pageCount: Int) : QuizData.AnswerArr {
        var answerArray: QuizData.AnswerArr = QuizData.AnswerArr(mutableListOf())
        val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val answers = pref.getString(ANSWERS, "")
        val mutableList = mutableListOf<Int>()
        for(i in 1..pageCount) { mutableList.add(0) }
        answerArray.answerArr = mutableList
        val gson = Gson()
        return if(answers == "") {
            answerArray
        } else {
            gson.fromJson(answers, QuizData.AnswerArr::class.java)
        }
    }
}
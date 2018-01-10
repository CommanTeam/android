package org.appjam.comman.youtube

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import org.appjam.comman.R


/**
 * Created by junhoe on 2018. 1. 1..
 */

class LectureVideoService : Service(), AudioExtractor.MediaStartListener {

    private var params : WindowManager.LayoutParams? = null
    private var windowManager : WindowManager? = null
    private var rootView : View? = null


    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val TAG = "LectureVideoService"
        const val PLAY_TIME_EXTRA = "playTimeExtra"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager!!.defaultDisplay
        val width = (display.width * 0.9).toInt() //Display 사이즈의 90%

        params = WindowManager.LayoutParams(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)


        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rootView  = layoutInflater.inflate(R.layout.floating_button_icon, null)
        windowManager?.addView(rootView, params)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        setDraggable(rootView!!)
//        windowManager?.updateViewLayout(rootView, params)



        //val progressTime = intent?.getIntExtra(PLAY_TIME_EXTRA, 0)
        val videoId = intent?.getStringExtra(YouTubeConfigs.VIDEO_ID)
        val youtubeLink = YouTubeConfigs.URL_PREFIX + videoId
        Log.i(TAG, "Youtube Link : $youtubeLink")
        mediaPlayer = MediaPlayer()

        // 유튜브 비디오에서 오디오를 추출하는 라이브러리의 구현체를 이용함
        val extractor = AudioExtractor(this@LectureVideoService, mediaPlayer)
        extractor.listener = this
        extractor.execute(youtubeLink)
        return START_STICKY
    }

    override fun onAudioStarted(mediaPlayer: MediaPlayer?) {
    }

    private fun setDraggable(rootView: View) {
        rootView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                var initialX: Float = 0F
                var initialY: Float = 0F
                var initialTouchX: Float = 0F
                var initialTouchY: Float = 0F
                when (p1!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = p0!!.x
                        initialY = p0.y
                        initialTouchX = p1.rawX
                        initialTouchY = p1.rawY
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        p0!!.x = initialX + (p1.rawX - initialTouchX)
                        p0.y = initialY + (p1.rawY - initialTouchY)
                        rootView.x = p0.x
                        rootView.y = p0.y
                        return true
                    }
                }
                return false
            }
        })
    }

}



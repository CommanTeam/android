package org.appjam.comman.youtube

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import org.appjam.comman.R


/**
 * Created by junhoe on 2018. 1. 1..
 */

class LectureVideoService : Service(), AudioExtractor.MediaStartListener {

    private var params : WindowManager.LayoutParams? = null
    private val windowManager : WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private var rootView : View? = null


    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val TAG = "LectureVideoService"
        const val PLAY_TIME_EXTRA = "playTimeExtra"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        val display = windowManager.defaultDisplay
        val width = (display.width * 0.9).toInt() //Display 사이즈의 90%
        val height = (display.height * 0.6).toInt()

        params = WindowManager.LayoutParams(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)


        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rootView  = layoutInflater.inflate(R.layout.floating_button_icon, null)

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        addViewToWindow()

//        windowManager?.updateViewLayout(rootView, params)

        //val progressTime = intent?.getIntExtra(PLAY_TIME_EXTRA, 0)
        val videoId = intent?.getStringExtra(YouTubeConfigs.VIDEO_ID)
        val youtubeLink = YouTubeConfigs.URL_PREFIX + videoId
        Log.i(TAG, "Youtube Link : $youtubeLink")
        mediaPlayer = MediaPlayer()

        rootView?.findViewById<Button>(R.id.floating_wide_screnn_btn)?.setOnClickListener {
            val intent = Intent(applicationContext, YoutubePracticeActivity::class.java)
            stopService(intent)
        }

        // 유튜브 비디오에서 오디오를 추출하는 라이브러리의 구현체를 이용함
        val extractor = AudioExtractor(this@LectureVideoService, mediaPlayer)
        extractor.listener = this
        extractor.execute(youtubeLink)
        return START_STICKY
    }

    private fun addViewToWindow() {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                windowManager.addView(rootView, params)
//                setDraggable(rootView)
            }

        })
    }

    override fun onAudioStarted(mediaPlayer: MediaPlayer?) {
    }

    var initialX: Float = 0F
    var initialY: Float = 0F
    var initialTouchX: Float = 0F
    var initialTouchY: Float = 0F
    private fun setDraggable(rootView: View?) {
        rootView?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = view.x
                        initialY = view.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.i(TAG, "on Action Move")
                        params?.x = (initialX + (event.rawX - initialTouchX)).toInt()
                        params?.y = (initialY + (event.rawY - initialTouchY)).toInt()
                        windowManager.updateViewLayout(rootView, params)
                        return true
                    }
                }
                return false
            }
        })
    }

}
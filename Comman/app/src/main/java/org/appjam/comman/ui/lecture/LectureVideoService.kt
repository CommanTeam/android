package org.appjam.comman.ui.lecture

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import org.appjam.comman.youtube.AudioExtractor
import org.appjam.comman.youtube.YouTubeConfigs



/**
 * Created by junhoe on 2018. 1. 1..
 */
class LectureVideoService : Service(), AudioExtractor.MediaStartListener {

    private var mediaPlayer: MediaPlayer? = null
    companion object {
        const val TAG = "LectureVideoService"
        const val PLAY_TIME_EXTRA = "playTimeExtra"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //val progressTime = intent?.getIntExtra(PLAY_TIME_EXTRA, 0)
        val videoId = intent?.getStringExtra(YouTubeConfigs.VIDEO_ID)
        val youtubeLink = YouTubeConfigs.URL_PREFIX + videoId
        mediaPlayer = MediaPlayer()

        // 유튜브 비디오에서 오디오를 추출하는 라이브러리의 구현체를 이용함
        val extractor = AudioExtractor(this@LectureVideoService, mediaPlayer)
        extractor.listener = this
        extractor.execute(youtubeLink)
        return START_NOT_STICKY
    }

    override fun onAudioStarted(mediaPlayer: MediaPlayer?) {
    }



}
package org.appjam.comman.ui.lecture

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_lecture_video.*
import org.appjam.comman.R
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.youtube.YouTubeConfigs

class LectureVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,
                                                    YouTubePlayer.PlaybackEventListener {
    override fun onSeekTo(p0: Int) {
    }

    override fun onBuffering(p0: Boolean) {
    }

    override fun onPlaying() {
    }

    override fun onStopped() {
        PrefUtils.putYoutubeCurrentTime(this, player!!)
    }

    override fun onPaused() {
        PrefUtils.putYoutubeCurrentTime(this, player!!)
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    companion object {
        val TAG = "LectureVideoActivity"
    }

    private var player: YouTubePlayer? = null
    private val videoId = "23h16yb-cpk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_video)
        Toast.makeText(this@LectureVideoActivity, "onCreate() 호출", Toast.LENGTH_SHORT).show()
        lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)
        land_lectureVideo_youtube_playerView?.initialize(YouTubeConfigs.API_KEY, this)

    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            this.player = player
            player.loadVideo(videoId)
            Toast.makeText(this@LectureVideoActivity, "onInitializationSuccess() 호출", Toast.LENGTH_SHORT).show()
//            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
        } else {
            player.play()
        }
    }


    override fun onResume() {
        super.onResume()
//        player?.loadVideo(videoId, PrefUtils.getInt(this, PrefUtils.CURRENT_TIME))
        Toast.makeText(this@LectureVideoActivity, "onResume() 호출", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(p0: Bundle?) {
        super.onSaveInstanceState(p0)
        Toast.makeText(this@LectureVideoActivity, "onSaveInstanceState() 호출", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this@LectureVideoActivity, "onPause() 호출", Toast.LENGTH_SHORT).show()
        // pause할 때 아이디를 intent에 넣어서 service를 실행, 후에 현재 재생시간 등등을 넣을 수 있음
//        val intent = Intent(this@LectureVideoActivity, LectureVideoService::class.java)
//        intent.putExtra(YouTubeConfigs.VIDEO_ID, videoId)
//        startService(intent)
    }
}



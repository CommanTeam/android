package org.appjam.comman.ui.lecture

import android.content.Intent
import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_lecture_video.*
import org.appjam.comman.R
import org.appjam.comman.youtube.YouTubeConfigs

class LectureVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private var player: YouTubePlayer? = null
    private val videoId = "wKJ9KzGQq0w"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_video)
        lectureVideo_youtube_playerView.initialize(YouTubeConfigs.API_KEY, this)


    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            this.player = player
            player.cueVideo(videoId)
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, result: YouTubeInitializationResult?) {

    }

    override fun onPause() {
        super.onPause()
        // pause할 때 아이디를 intent에 넣어서 service를 실행, 후에 현재 재생시간 등등을 넣을 수 있음
        val intent = Intent(this@LectureVideoActivity, LectureVideoService::class.java)
        intent.putExtra(YouTubeConfigs.VIDEO_ID, videoId)
        startService(intent)
    }
}

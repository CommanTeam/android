package org.appjam.comman.youtube

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.facebook.network.connectionclass.ConnectionClassManager
import com.facebook.network.connectionclass.ConnectionQuality

/**
 * Created by junhoe on 2018. 1. 2..
 * YoutubeExtractor의 컴만 버전에 맞는 구현체
 */
class AudioExtractor(context: Context, private val mediaPlayer: MediaPlayer?) : YouTubeExtractor(context) {

    companion object {
        const val TAG = "AudioExtractor"
    }
    var listener: MediaStartListener? = null

    interface MediaStartListener {
        fun onAudioStarted(mediaPlayer: MediaPlayer?)
    }

    /**
     * Audio File이 완성됨에 따라 소리를 바로 재생하도록 설정함
     * 재생하고 나서 정지를 할지 혹은 다른 행동을 할지에 대해서 후에 이 곳에서 조정하고 service단에서도 조절할 수 있는
     * 요소가 있을까 해서 listener도 부착함
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
        if (ytFiles == null) {
            return
        }
        Log.i(TAG, "in here")
        val ytFile = getBestStream(ytFiles)
        mediaPlayer?.apply {
            reset()
            setDataSource(ytFile.url)
            val attributes = AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            setAudioAttributes(attributes)
            prepare()
            start()
            listener?.onAudioStarted(mediaPlayer)
        }
    }

    // 인터넷 상황에 따른 최적의 음질을 선택
    private fun getBestStream(ytFiles: SparseArray<YtFile>): YtFile {

        val connectionQuality = ConnectionClassManager.getInstance().currentBandwidthQuality
        var itags = intArrayOf(251, 141, 140, 17)

        if (connectionQuality != null && connectionQuality != ConnectionQuality.UNKNOWN) {
            itags = when (connectionQuality) {
                ConnectionQuality.POOR -> intArrayOf(17, 140, 251, 141)
                ConnectionQuality.MODERATE -> intArrayOf(251, 141, 140, 17)
                else -> intArrayOf(141, 251, 140, 17)
            }
        }

        return when {
            ytFiles.get(itags[0]) != null -> ytFiles.get(itags[0])
            ytFiles.get(itags[1]) != null -> ytFiles.get(itags[1])
            ytFiles.get(itags[2]) != null -> ytFiles.get(itags[2])
            else -> ytFiles.get(itags[3])
        }
    }

}
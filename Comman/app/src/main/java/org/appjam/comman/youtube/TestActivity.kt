package org.appjam.comman.youtube

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*
import org.appjam.comman.R

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        buttonView.setOnClickListener {
            val intent = Intent(this@TestActivity, LectureVideoService::class.java)
            intent.putExtra(YouTubeConfigs.VIDEO_ID, "ph2vj5T3L8o")
            startService(intent)
        }
    }
}

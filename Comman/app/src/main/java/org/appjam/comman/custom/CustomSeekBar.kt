package org.appjam.comman.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.custom_youtube_seek_bar.view.*
import org.appjam.comman.R
import android.view.MotionEvent


/**
 * Created by junhoe on 2018. 1. 6..
 */
class CustomSeekBar : RelativeLayout {

    companion object {
        const val TAG = "CustomSeekBar"
    }
    interface CustomSeekBarListener {
        fun onThumbDragged(progress: Int)
        fun onProgressBarTouched(progress: Int)
    }

    private var bar: ProgressBar? = null
    private var thumb: ImageView? = null
    private var listener: CustomSeekBarListener? = null
    private var deltaX = 0f

    var max: Int = 0
        set(value) {
            bar?.max = value
            field = value
        }

    var progress: Int = 0
        set(value) {
            bar?.progress = value
            if (max != 0) {
                thumb?.apply {
                    val nextLaoutParams = layoutParams as RelativeLayout.LayoutParams
                    val leftMargin = this@CustomSeekBar.width * value / max
                    Log.i(TAG, " left margin : $leftMargin")
                    nextLaoutParams.leftMargin = (leftMargin - width / 2)
                    layoutParams = nextLaoutParams
                    this@CustomSeekBar.invalidate()
                }
            } else {
                thumb?.apply {
                    val nextLayoutParams = layoutParams as RelativeLayout.LayoutParams
                    nextLayoutParams.leftMargin = 0
                    layoutParams = nextLayoutParams
                    this@CustomSeekBar.invalidate()
                }
            }
            field = value
        }
    constructor(context: Context) : super(context) {
        initView(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    private fun initView(context: Context) {

        val inflaterService = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater = context.getSystemService(inflaterService) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.custom_youtube_seek_bar, this, false)
        addView(view)

        this.thumb = view.seekBarThumb
        view.seekBarThumb.setOnTouchListener { view, event ->
            val action = event.action
            val rawXPos = event.rawX
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                    deltaX = rawXPos - layoutParams.leftMargin
                }
                MotionEvent.ACTION_MOVE -> {
                    progress = (rawXPos - deltaX).toInt() * max / width
                }
                else -> {
                }
            }
            listener?.onThumbDragged(bar?.progress ?: 0)
            true
        }

        this.bar = view.progressBar
        setOnTouchListener { v, event ->
            progress = (event.x / width * max).toInt()
            listener?.onProgressBarTouched(bar?.progress ?: 0)
            true
        }
    }

    fun setSeekBarListener(listener: CustomSeekBarListener) {
        this.listener = listener
    }

    override fun onDetachedFromWindow() {
        listener = null
        super.onDetachedFromWindow()
    }
}
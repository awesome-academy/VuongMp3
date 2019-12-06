package com.vuongnq14798.vuongmp3.ui.playmusic

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import com.vuongnq14798.vuongmp3.R
import com.vuongnq14798.vuongmp3.base.BaseActivity
import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.service.MediaPlayerService
import com.vuongnq14798.vuongmp3.service.ServiceListener
import com.vuongnq14798.vuongmp3.util.*
import kotlinx.android.synthetic.main.activity_play_music.*

class PlayMusicActivity : BaseActivity(),
    ServiceListener,
    View.OnClickListener,
    SeekBar.OnSeekBarChangeListener {

    private lateinit var mediaPlayerService: MediaPlayerService
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.MediaPlayerBinder
            mediaPlayerService = binder.getService()
            mediaPlayerService.setServiceListener(this@PlayMusicActivity)
            mediaPlayerService.getCurrentTrack()?.let { bindData(it) }
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }
    override val layoutResId: Int get() = R.layout.activity_play_music

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
        play.setOnClickListener(this)
        back.setOnClickListener(this)
        next.setOnClickListener(this)
        previous.setOnClickListener(this)
        loop.setOnClickListener(this)
        shuffle.setOnClickListener(this)
        seekTime.setOnSeekBarChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        bindService(MediaPlayerService.getIntent(this), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }

    fun bindData(track: Track) {
        textTrackName.text = track.title
        textArtistName.text = track.artist
        track.artworkUrl?.let { ImageUtils.loadCircleImage(artwork, it) }
        duration.text = track.duration.let { StringUtils.formatTime(it) }
        updateCurrentTime()
        ImageUtils.rotateImage(artwork)
        updateStateIcon()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.back -> onBackPressed()
            R.id.shuffle -> onClickShuffle()
            R.id.previous -> mediaPlayerService.previousTrack()
            R.id.play -> onClickPlayPause()
            R.id.next -> mediaPlayerService.nextTrack()
            R.id.loop -> onClickLoop()
        }
    }

    override fun onChangeTrackListener() {
        mediaPlayerService.getCurrentTrack()?.let { bindData(it) }
    }

    override fun onPlayingStateListener(state: Int) {
        if (state == StateType.PAUSED) {
            play.setImageResource(R.drawable.ic_play)
        } else {
            play.setImageResource(R.drawable.ic_pause)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (progress > 0) mediaPlayerService.seekTo(progress * TIME_SECOND)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private fun updateStateIcon() {
        if (mediaPlayerService.isPlaying()) {
            play.setImageResource(R.drawable.ic_pause)
        } else {
            play.setImageResource(R.drawable.ic_play)
        }

        when (mediaPlayerService.getLoopType()) {
            LoopType.NONE -> loop.setColorFilter(ActivityCompat.getColor(this, R.color.color_white))
            LoopType.ALL -> loop.setColorFilter(ActivityCompat.getColor(this, R.color.color_green))
            LoopType.ONE -> loop.setColorFilter(ActivityCompat.getColor(this, R.color.color_accent))
        }

        when (mediaPlayerService.getShuffleType()) {
            ShuffleType.OFF -> shuffle.setColorFilter(ActivityCompat.getColor(this, R.color.color_white))
            ShuffleType.ON -> shuffle.setColorFilter(ActivityCompat.getColor(this, R.color.color_green))
        }
    }
    private fun updateCurrentTime() {
        seekTime.max = mediaPlayerService.getCurrentTrack()?.duration?.div(TIME_SECOND) ?: 0
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (mediaPlayerService.isPlaying()) {
                    val current = mediaPlayerService.getCurrentPosition().div(TIME_SECOND)
                    seekTime.progress = current
                    currentTime.text =
                        StringUtils.formatTime(mediaPlayerService.getCurrentPosition())
                }
                handler.postDelayed(this, TIME_DELAY)
            }
        }, TIME_DELAY)
    }

    private fun onClickShuffle() {
        when (mediaPlayerService.getShuffleType()) {
            ShuffleType.OFF -> {
                mediaPlayerService.setShuffleType(ShuffleType.ON)
                shuffle.setColorFilter(ActivityCompat.getColor(this, R.color.color_green))
            }
            ShuffleType.ON -> {
                mediaPlayerService.setShuffleType(ShuffleType.OFF)
                shuffle.setColorFilter(ActivityCompat.getColor(this, R.color.color_white))
            }
        }
    }

    private fun onClickPlayPause() {
        if (mediaPlayerService.isPlaying()) {
            mediaPlayerService.pauseTrack()
        } else {
            mediaPlayerService.startTrack()
        }
    }

    private fun onClickLoop() {
        when (mediaPlayerService.getLoopType()) {
            LoopType.NONE -> {
                mediaPlayerService.setLoopType(LoopType.ALL)
                loop.setColorFilter(ActivityCompat.getColor(this, R.color.color_green))
            }
            LoopType.ALL -> {
                mediaPlayerService.setLoopType(LoopType.ONE)
                loop.setColorFilter(ActivityCompat.getColor(this, R.color.color_accent))

            }
            LoopType.ONE -> {
                mediaPlayerService.setLoopType(LoopType.NONE)
                loop.setColorFilter(ActivityCompat.getColor(this, R.color.color_white))
            }
        }
    }

    companion object {
        const val TIME_SECOND = 1000
        const val TIME_DELAY = 1000L
        fun getIntent(context: Context) = Intent(context, PlayMusicActivity::class.java)
    }
}

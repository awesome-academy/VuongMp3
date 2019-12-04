package com.vuongnq14798.vuongmp3.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.vuongnq14798.vuongmp3.R
import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.mediaplayer.MediaPlayerManager
import com.vuongnq14798.vuongmp3.util.LoopType
import com.vuongnq14798.vuongmp3.util.StateType

class MediaPlayerService : Service(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener,
    ServiceInterface {

    private lateinit var binder: IBinder
    private lateinit var listener: ServiceListener

    private val mediaPlayerManager: MediaPlayerManager by lazy { MediaPlayerManager.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        binder = MediaPlayerBinder()
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayerManager.start()
    }

    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean = run {
        onFailure()
        true
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        when (mediaPlayerManager.loopType) {
            LoopType.NONE -> {
                if (mediaPlayerManager.isEndOfList()) mediaPlayerManager.stop()
                else nextTrack()
            }
            LoopType.ALL -> nextTrack()

            LoopType.ONE -> mediaPlayerManager.start()
        }
    }

    override fun onDestroy() {
        mediaPlayerManager.release()
        super.onDestroy()
    }

    override fun changeTrack(track: Track) = mediaPlayerManager.changeTrack(track)

    override fun pauseTrack() {
        mediaPlayerManager.pause()
        listener.onPlayingStateListener(StateType.PAUSED)
    }

    override fun startTrack() {
        mediaPlayerManager.start()
        listener.apply {
            onChangeTrackListener()
            onPlayingStateListener(StateType.PLAYING)
        }
    }

    override fun previousTrack() {
        mediaPlayerManager.previousTrack()
        listener.onChangeTrackListener()
    }

    override fun nextTrack() {
        mediaPlayerManager.nextTrack()
        listener.onChangeTrackListener()
    }

    override fun isPlaying(): Boolean = mediaPlayerManager.isPlaying

    override fun seekTo(position: Int) = mediaPlayerManager.seekTo(position)

    override fun getCurrentTrack(): Track? = mediaPlayerManager.currentTrack

    override fun setCurrentTrack(track: Track?) {
        mediaPlayerManager.currentTrack = track
    }

    override fun getCurrentPosition() = mediaPlayerManager.currentPosition

    override fun setServiceListener(listener: ServiceListener) {
        this.listener = listener
    }

    override fun addTrack(track: Track) = mediaPlayerManager.addTrack(track)

    override fun updateTracks(newTracks: List<Track>) = mediaPlayerManager.updateTracks(newTracks)

    override fun getShuffleType(): Int = mediaPlayerManager.shuffleType

    override fun setShuffleType(type: Int) {
        mediaPlayerManager.shuffleType = type
    }

    override fun getLoopType(): Int = mediaPlayerManager.loopType

    override fun setLoopType(type: Int) {
        mediaPlayerManager.loopType = type
    }

    override fun removeTrack(track: Track) = mediaPlayerManager.removeTrack(track)

    override fun clearTracks() = mediaPlayerManager.clearTracks()

    override fun onFailure() = this.toast(getString(R.string.service_on_failure))

    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    inner class MediaPlayerBinder : Binder() {
        fun getService(): MediaPlayerService = this@MediaPlayerService
    }

    companion object {

        private var INSTANCE: MediaPlayerService? = null

        fun getInstance() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MediaPlayerService().also { INSTANCE = it }
        }

        fun getIntent(context: Context) = Intent(context, MediaPlayerService::class.java)
    }
}

package com.vuongnq14798.vuongmp3.ui.mymusic

import android.os.Bundle
import android.widget.Toast
import com.vuongnq14798.vuongmp3.MainActivity
import com.vuongnq14798.vuongmp3.R
import com.vuongnq14798.vuongmp3.base.BaseFragment
import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.data.source.TracksRepository
import com.vuongnq14798.vuongmp3.data.source.local.TracksLocalDataSource
import com.vuongnq14798.vuongmp3.data.source.remote.TracksRemoteDataSource
import com.vuongnq14798.vuongmp3.ui.playmusic.PlayMusicActivity
import com.vuongnq14798.vuongmp3.util.TrackDiffCallBack
import kotlinx.android.synthetic.main.fragment_my_music.*
import kotlinx.android.synthetic.main.fragment_my_music.fab

class MyMusicFragment : BaseFragment(),
    MyMusicContract.View,
    TrackAdapter.OnTrackClickListener {

    private val tracksRepository: TracksRepository? by lazy {
        context?.let {
            TracksRepository.getInstance(
                TracksRemoteDataSource.getInstance(),
                TracksLocalDataSource.getInstance(it)
            )
        }
    }

    private val myMusicPresenter: MyMusicPresenter? by lazy { tracksRepository?.let { MyMusicPresenter(it, this) } }

    private val tracks = mutableListOf<Track>()

    override val layoutResId: Int = R.layout.fragment_my_music

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
        myMusicPresenter?.getTracksLocal()

        fab.setOnClickListener {
            (activity as MainActivity).getMediaPlayerService()?.let {
                it.updateTracks(tracks)
                it.changeTrack(tracks[0])
                context?.let { startActivity(PlayMusicActivity.getIntent(it)) }
            }
        }
    }

    override fun showTracks(tracks: List<Track>) {
        this.tracks.addAll(tracks)
        val trackAdapter = TrackAdapter(TrackDiffCallBack(), this)
        recyclerMyMusic.adapter = trackAdapter
        trackAdapter.submitList(tracks)
    }

    override fun showError(exception: Exception) {
        Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
    }

    override fun onTrackClicked(track: Track) {
        (activity as MainActivity).getMediaPlayerService()?.changeTrack(track)
        context?.let { startActivity(PlayMusicActivity.getIntent(it)) }

    }
}

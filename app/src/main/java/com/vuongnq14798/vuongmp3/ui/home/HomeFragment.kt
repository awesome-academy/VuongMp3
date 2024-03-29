package com.vuongnq14798.vuongmp3.ui.home

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import com.vuongnq14798.vuongmp3.MainActivity
import com.vuongnq14798.vuongmp3.R
import com.vuongnq14798.vuongmp3.base.BaseFragment
import com.vuongnq14798.vuongmp3.data.model.Genre
import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.data.source.TracksRepository
import com.vuongnq14798.vuongmp3.data.source.local.TracksLocalDataSource
import com.vuongnq14798.vuongmp3.data.source.remote.TracksRemoteDataSource
import com.vuongnq14798.vuongmp3.service.MediaPlayerService
import com.vuongnq14798.vuongmp3.ui.detailgenre.DetailGenreActivity
import com.vuongnq14798.vuongmp3.ui.playmusic.PlayMusicActivity
import com.vuongnq14798.vuongmp3.util.Constants
import com.vuongnq14798.vuongmp3.util.GenreDiffCallBack
import com.vuongnq14798.vuongmp3.util.TrackDiffCallBack
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class HomeFragment : BaseFragment(),
    HomeContract.View,
    GenreAdapter.OnGenreClickListener,
    TrackAdapter.OnTrackClickListener {

    private lateinit var mediaPlayerService: MediaPlayerService
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.MediaPlayerBinder
            mediaPlayerService = binder.getService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    private val tracksRepository: TracksRepository? by lazy {
        context?.let {
            TracksRepository.getInstance(
                TracksRemoteDataSource.getInstance(),
                TracksLocalDataSource.getInstance(it)
            )
        }
    }

    private val homePresenter: HomePresenter? by lazy { tracksRepository?.let { HomePresenter(it, this) } }

    override val layoutResId: Int = R.layout.fragment_home

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
        homePresenter?.getGenres()
        homePresenter?.getTracksRemote(Constants.Genre.GENRES_ALL_MUSIC)
    }

    override fun onStart() {
        super.onStart()
        activity?.bindService(
            context?.let { MediaPlayerService.getIntent(it) },
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun showGenres(genres: List<Genre>) {
        val genreAdapter = GenreAdapter(GenreDiffCallBack(), this)
        suggestedPlaylists.adapter = genreAdapter
        genreAdapter.submitList(genres)
    }

    override fun showTracks(tracks: List<Track>) {
        val trackAdapter = TrackAdapter(TrackDiffCallBack(), this)
        topTrending.adapter = trackAdapter
        trackAdapter.submitList(tracks)
    }

    override fun showError(exception: Exception) {
        Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
    }

    override fun onGenreClicked(genre: Genre) =
        startActivity(context?.let { DetailGenreActivity.getIntent(it, genre) })

    override fun onTrackClicked(track: Track) {
        mediaPlayerService.changeTrack(track)
        context?.let { startActivity(PlayMusicActivity.getIntent(it)) }
    }
}

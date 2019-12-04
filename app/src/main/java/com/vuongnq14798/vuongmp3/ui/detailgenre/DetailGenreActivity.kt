package com.vuongnq14798.vuongmp3.ui.detailgenre

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.vuongnq14798.vuongmp3.R
import com.vuongnq14798.vuongmp3.base.BaseActivity
import com.vuongnq14798.vuongmp3.data.model.Genre
import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.data.source.TracksRepository
import com.vuongnq14798.vuongmp3.data.source.local.TracksLocalDataSource
import com.vuongnq14798.vuongmp3.data.source.remote.TracksRemoteDataSource
import com.vuongnq14798.vuongmp3.util.TrackDiffCallBack
import kotlinx.android.synthetic.main.activity_detail_genre.*
import java.lang.Exception
import java.util.*

class DetailGenreActivity : BaseActivity(), DetailGenreContract.View,
    DetailGenreAdapter.OnTrackClickListener {

    private lateinit var genre: Genre

    private val tracksRepository: TracksRepository by lazy {
        TracksRepository(
            TracksRemoteDataSource.getInstance(),
            TracksLocalDataSource.getInstance(this)
        )
    }
    private val detailGenrePresenter: DetailGenrePresenter by lazy {
        DetailGenrePresenter(
            tracksRepository,
            this
        )
    }
    override val layoutResId: Int = R.layout.activity_detail_genre

    override fun initData(savedInstanceState: Bundle?) {
        genre = intent.getParcelableExtra(GENRE_INFO)
    }

    override fun initComponents() {

        detailGenrePresenter.getTracksRemote(genre.genreURL)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar.title = getString(R.string.all_audio).toUpperCase(Locale.getDefault())
        genreImage.setImageResource(genre.genreImage)
    }

    override fun showTracks(tracks: List<Track>) {
        val trackAdapter = DetailGenreAdapter(TrackDiffCallBack(), this)
        detailGenre.adapter = trackAdapter
        trackAdapter.submitList(tracks)
    }

    override fun showError(exception: Exception) {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
    }

    override fun onTrackClicked(Track: Track) {
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        private const val GENRE_INFO = "genre_info"
        fun getIntent(context: Context, genre: Genre): Intent =
            Intent(context, DetailGenreActivity::class.java).putExtra(GENRE_INFO, genre)
    }
}

package com.vuongnq14798.vuongmp3.ui.home

import android.os.Bundle
import com.vuongnq14798.vuongmp3.R
import com.vuongnq14798.vuongmp3.base.BaseFragment
import com.vuongnq14798.vuongmp3.data.source.TracksRepository
import com.vuongnq14798.vuongmp3.data.source.local.TracksLocalDataSource
import com.vuongnq14798.vuongmp3.data.source.remote.TracksRemoteDataSource
import com.vuongnq14798.vuongmp3.util.Constants

class HomeFragment : BaseFragment(), HomeContract.View {

    private val tracksRepository: TracksRepository by lazy {
        TracksRepository(TracksRemoteDataSource.getInstance(), TracksLocalDataSource.getInstance())
    }
    private val homePresenter: HomePresenter by lazy { HomePresenter(tracksRepository, this) }

    override val layoutResId: Int = R.layout.fragment_home

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
        homePresenter.getTracksRemote(Constants.Genre.GENRES_ALL_AUDIO)
    }
}

package com.vuongnq14798.vuongmp3.data.source.remote

import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.data.source.TracksDataSource
import com.vuongnq14798.vuongmp3.ui.OnDataLoadedListener

class TracksRemoteDataSource : TracksDataSource.Remote {

    override fun getTracksRemote(
        genre: String,
        callback: OnDataLoadedListener<List<Track>>
    ) {
        TracksAsyncTask(callback).execute(genre)
    }

    override fun searchTracksRemote(
        searchKey: String,
        callback: OnDataLoadedListener<List<Track>>
    ) {

    }

    companion object {

        private var INSTANCE: TracksRemoteDataSource? = null

        fun getInstance() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: TracksRemoteDataSource().also { INSTANCE = it }
        }
    }
}

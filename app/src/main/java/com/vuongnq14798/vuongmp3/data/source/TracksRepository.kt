package com.vuongnq14798.vuongmp3.data.source

import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.data.source.local.TracksLocalDataSource
import com.vuongnq14798.vuongmp3.data.source.remote.TracksRemoteDataSource

class TracksRepository(
    private val remoteDataSource: TracksDataSource.Remote,
    private val localDataSource: TracksDataSource.Local
) : TracksDataSource.Remote, TracksDataSource.Local {

    override fun getTracksRemote(
        genre: String,
        callback: TracksDataSource.LoadTracksCallback<List<Track>>
    ) {
        remoteDataSource.getTracksRemote(genre, callback)
    }

    override fun searchTracksRemote(
        searchKey: String,
        callback: TracksDataSource.LoadTracksCallback<List<Track>>
    ) {
        remoteDataSource.searchTracksRemote(searchKey, callback)
    }

    companion object {
        private var INSTANCE: TracksRepository? = null
        fun getInstance(
            remoteDataSource: TracksRemoteDataSource,
            localDataSource: TracksLocalDataSource
        ) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: TracksRepository(remoteDataSource, localDataSource).also { INSTANCE = it }
        }
    }
}

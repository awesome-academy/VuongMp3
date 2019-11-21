package com.vuongnq14798.vuongmp3.data.source.remote

import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.data.source.TracksDataSource

class TracksRemoteDataSource: TracksDataSource.Remote {

    override fun getTracksRemote(
        genre: String,
        callback: TracksDataSource.LoadTracksCallback<List<Track>>
    ) { }

    override fun searchTracksRemote(
        searchKey: String,
        callback: TracksDataSource.LoadTracksCallback<List<Track>>
    ) { }
}

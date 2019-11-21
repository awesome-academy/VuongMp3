package com.vuongnq14798.vuongmp3.data.source

import com.vuongnq14798.vuongmp3.data.model.Track

interface TracksDataSource {

    interface LoadTracksCallback<T> {

        fun onTracksLoaded(data: T)
        fun onDataNotAvailable()
    }

    interface Remote {

        fun getTracksRemote(genre: String, callback: LoadTracksCallback<List<Track>>)
        fun searchTracksRemote(searchKey: String, callback: LoadTracksCallback<List<Track>>)
    }

    interface Local { }
}

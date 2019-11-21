package com.vuongnq14798.vuongmp3.data.source.local

import com.vuongnq14798.vuongmp3.data.source.TracksDataSource

class TracksLocalDataSource : TracksDataSource.Local {

    companion object {

        private var INSTANCE: TracksLocalDataSource? = null

        fun getInstance() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: TracksLocalDataSource().also { INSTANCE = it }
        }
    }
}

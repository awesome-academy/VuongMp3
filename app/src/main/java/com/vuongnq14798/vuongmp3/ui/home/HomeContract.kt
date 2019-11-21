package com.vuongnq14798.vuongmp3.ui.home

interface HomeContract {

    interface View {}

    interface Presenter {
        fun getTracksRemote (genre: String)
    }
}

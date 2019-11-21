package com.vuongnq14798.vuongmp3.data.model

import com.vuongnq14798.vuongmp3.util.Constants
import com.vuongnq14798.vuongmp3.util.StringUtils
import org.json.JSONObject

data class Track(
    val id: Int,
    val title: String,
    val duration: Int,
    val likesCount: Int = 0,
    val isDownload: Boolean,
    val streamUrl: String,
    val username: String,
    val artworkUrl: String?,
    val downloadUrl: String?,
    val artist: String?,
    val description: String?
) {
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getInt(Constants.Track.ID),
        jsonObject.getString(Constants.Track.TITLE),
        jsonObject.getInt(Constants.Track.DURATION),
        jsonObject.getInt(Constants.Track.LIKE_COUNT),
        jsonObject.getBoolean(Constants.Track.IS_DOWNLOAD),
        StringUtils.initStreamUrl(jsonObject.getInt(Constants.Track.ID).toString()),
        jsonObject.getJSONObject(Constants.Track.USER).getString(Constants.Track.USERNAME),
        jsonObject.getString(Constants.Track.ARTWORK_URL),
        jsonObject.getString(Constants.Track.DOWNLOAD_URL),
        if (!jsonObject.isNull(Constants.Track.PUBLISHER_METADATA)
            && !jsonObject.getJSONObject(Constants.Track.PUBLISHER_METADATA)
                .isNull(Constants.Track.ARTIST)
        ) {
            jsonObject.getJSONObject(Constants.Track.PUBLISHER_METADATA)
                .getString(Constants.Track.ARTIST)
        } else {
            jsonObject.getJSONObject(Constants.Track.USER).getString(Constants.Track.USERNAME)
        },
        jsonObject.getString(Constants.Track.DESCRIPTION)
    )
}

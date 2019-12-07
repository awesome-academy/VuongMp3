package com.vuongnq14798.vuongmp3.data.source.local

import android.content.Context
import android.os.AsyncTask
import android.provider.BaseColumns
import android.provider.MediaStore
import com.vuongnq14798.vuongmp3.data.model.Track
import com.vuongnq14798.vuongmp3.ui.OnDataLoadedListener

class TracksLocalAsyncTask(
    private val context: Context,
    private val callback: OnDataLoadedListener<List<Track>>
) : AsyncTask<String, Track, List<Track>>() {

    override fun doInBackground(vararg params: String?): List<Track> {

        val tracks = mutableListOf<Track>()
        val projection = arrayOf(
            BaseColumns._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST
        )
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null) ?: return tracks
        while (cursor.moveToNext()) tracks.add(Track(cursor))
        cursor?.close()
        return tracks
    }

    override fun onPostExecute(result: List<Track>) {
        super.onPostExecute(result)
        callback.onSuccess(result)
    }
}

package com.mymusic.modules.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import java.io.File

class DownloadService(private val context: Context) {
    private val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    fun startDownload(path: String, name: String) {
        val request = DownloadManager.Request(Uri.parse(path))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setDestinationUri(Uri.fromFile(getAppMusicStorageDir(name)))
            .setTitle(name)
        downloadManager.enqueue(request)
    }

    private fun getAppMusicStorageDir(fileName: String): File {
        return File(context.filesDir, "hihigi.mp3")
    }
}
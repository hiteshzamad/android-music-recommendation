package com.mymusic

import android.util.Log

object L {

    fun p(s: String) {
        Log.println(Log.ASSERT, "MUSIC ${System.currentTimeMillis()}", s)
    }
}
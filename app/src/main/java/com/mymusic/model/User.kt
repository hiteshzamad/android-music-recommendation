package com.mymusic.model

data class User(
    val name: String,
    val gender: String,
    val dob: String,
    var email: String? = null
)

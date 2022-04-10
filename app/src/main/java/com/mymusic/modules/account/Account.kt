package com.mymusic.modules.account

data class Account(
    val name: String,
    val gender: String,
    val dob: String,
    var email: String? = null
)

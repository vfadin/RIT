package com.example.rit.utils

import android.content.Context

fun Context.setChosenApi(chosenApi: Constants.Api) {
    this.getSharedPreferences("api", Context.MODE_PRIVATE)
        .edit().putInt("chosen_api", chosenApi.ordinal).apply()
}

fun Context.restoreChosenApi(): Int {
    return this.getSharedPreferences("api", Context.MODE_PRIVATE)
        .getInt("chosen_api", -1)
}
package dev.ranga.recipeexplorer.analytics

import android.util.Log
import javax.inject.Inject

class LoggerImpl @Inject constructor() : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
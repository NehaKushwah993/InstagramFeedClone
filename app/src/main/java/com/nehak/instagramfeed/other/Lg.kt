package com.nehak.instagramfeed.other

import android.util.Log

/**
 * Create By Neha Kushwah
 */
class Lg {

    companion object {
        fun v(tag: String, log: String) {
            Log.v(tag, log)
        }

        fun printStackTrace(e: Exception) {
            e.printStackTrace()
        }

    }
}

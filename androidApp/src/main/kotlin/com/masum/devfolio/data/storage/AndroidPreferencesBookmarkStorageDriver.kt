package com.masum.devfolio.data.storage

import android.content.Context
import com.masum.devfolio.repository.BookmarkStorageDriver

class AndroidPreferencesBookmarkStorageDriver(context: Context) : BookmarkStorageDriver {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun loadBookmarks(): String? {
        return prefs.getString(KEY_BOOKMARKS, null)
    }

    override fun saveBookmarks(json: String) {
        prefs.edit().putString(KEY_BOOKMARKS, json).apply()
    }

    companion object {
        private const val PREFS_NAME = "devfolio_bookmarks"
        private const val KEY_BOOKMARKS = "bookmarks_json"
    }
}

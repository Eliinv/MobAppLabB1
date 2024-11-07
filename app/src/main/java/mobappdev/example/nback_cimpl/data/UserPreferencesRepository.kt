package mobappdev.example.nback_cimpl.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * This repository provides a way to interact with the DataStore api,
 * with this API you can save key:value pairs
 *
 * Currently this file contains only one thing: getting the highscore as a flow
 * and writing to the highscore preference.
 * (a flow is like a waterpipe; if you put something different in the start,
 * the end automatically updates as long as the pipe is open)
 *
 * Date: 25-08-2023
 * Version: Skeleton code version 1.0
 * Author: Yeetivity
 *
 */


class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val HIGHSCORE_KEY = intPreferencesKey("highscore")
        private const val TAG = "UserPreferencesRepo"
    }

    // Flow that emits the highscore value from DataStore
    val highscore: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Get the highscore or return 0 if it doesn't exist
            preferences[HIGHSCORE_KEY] ?: 0
        }

    // Function to save a new highscore value to DataStore
    suspend fun saveHighscore(score: Int) {
        dataStore.edit { preferences ->
            preferences[HIGHSCORE_KEY] = score
        }
    }
}
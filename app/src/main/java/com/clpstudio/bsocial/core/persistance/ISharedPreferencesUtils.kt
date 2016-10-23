package com.clpstudio.bsocial.core.persistance

import android.content.SharedPreferences

/**
 * Interface for interacting with the shared preferences in androud
 */
interface ISharedPreferencesUtils {

    /**
     * Register listener for shared preferences changes
     * @param listener The listener
     */
    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    /**
     * Unregister listener for shared preferences changes
     * @param listener The listener to be unregistered
     */
    fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)


}

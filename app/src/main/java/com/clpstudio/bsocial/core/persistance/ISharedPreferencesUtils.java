package com.clpstudio.bsocial.core.persistance;

import android.content.SharedPreferences;

/**
 * Created by DealDash
 * Interface for interacting with the shared preferences in androud
 */
public interface ISharedPreferencesUtils {

    /**
     * Register listener for shared preferences changes
     * @param listener The listener
     */
    void registerOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);

    /**
     * Unregister listener for shared preferences changes
     * @param listener The listener to be unregistered
     */
    void unregisterOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);



}

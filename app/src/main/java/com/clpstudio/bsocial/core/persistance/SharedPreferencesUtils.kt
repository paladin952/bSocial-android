package com.clpstudio.bsocial.core.persistance

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.clpstudio.bsocial.data.Session
import java.util.*
import javax.inject.Inject

/**
 * Concrete implementation for interacting with shared preferences
 */
class SharedPreferencesUtils
/**
 * The constructor

 * @param mContext The application;s mContext
 */
@Inject
constructor(
        /**
         * Application's mContext
         */
        private val mContext: Context, private val mSession: Session) : ISharedPreferencesUtils {

    /**
     * The shared preferences object
     */
    private val mGlobalSharedPreferences: SharedPreferences


    init {
        mGlobalSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    override fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        userPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key -> listener.onSharedPreferenceChanged(sharedPreferences, key) }

        mGlobalSharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key -> listener.onSharedPreferenceChanged(sharedPreferences, key) }
    }

    override fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        userPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        mGlobalSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Set the value of the preference

     * @param key   The key
     * *
     * @param value The value
     */
    private fun setPreferenceValue(key: String, value: Boolean) {
        getPreferences(key).edit().putBoolean(key, value).apply()
    }

    /**
     * Set the value of the preference

     * @param key   The key
     * *
     * @param value The value
     */
    private fun setPreferenceValue(key: String, value: String) {
        getPreferences(key).edit().putString(key, value).apply()
    }

    /**
     * Set the value of the preference

     * @param key   The key
     * *
     * @param value The value
     */
    private fun setPreferenceValue(key: String, value: Int) {
        getPreferences(key).edit().putInt(key, value).apply()
    }

    /**
     * Set the value of the preference

     * @param key   The key
     * *
     * @param value The value
     */
    private fun setPreferenceValue(key: String, value: Long) {
        getPreferences(key).edit().putLong(key, value).apply()
    }

    /**
     * Set the value of the preference

     * @param key The key
     */
    private fun getPreferenceValue(key: String, defaultValue: Boolean): Boolean {
        return getPreferences(key).getBoolean(key, defaultValue)
    }

    /**
     * get the value of the preference

     * @param key The key
     */
    private fun getPreferenceValue(key: String, defaultValue: Int): Int {
        return getPreferences(key).getInt(key, defaultValue)
    }

    /**
     * get the value of the preference

     * @param key The key
     */
    private fun getPreferenceValue(key: String, defaultValue: Long): Long {
        return getPreferences(key).getLong(key, defaultValue)
    }

    /**
     * get the value of the preference

     * @param key The key
     */
    private fun getPreferenceValue(key: String, defaultValue: String): String {
        return getPreferences(key).getString(key, defaultValue)
    }

    /**
     * Returning preferences for the current logged in user

     * @return The SharedPreferences
     */
    private val userPreferences: SharedPreferences
        get() = mContext.getSharedPreferences(sharedPreferencesUserSpecificName, Context.MODE_PRIVATE)

    /**
     * Get the sharedPreferences based on the the key

     * @param key The key
     * *
     * @return Global or UserSpecificPreferences based on the key
     */
    private fun getPreferences(key: String): SharedPreferences {
        return if (mGlobalSettingsKeys.contains(key)) mGlobalSharedPreferences else userPreferences
    }

    /**
     * @return The name of the sharedPreferences for the current logged in user
     */
    val sharedPreferencesUserSpecificName: String
        get() {
            return mContext.packageName + SharedPreferencesConstants.USER_SPECIFIC_FILE_NAME_PLACEHOLDER + mSession.user!!.id
        }

    companion object {

        /**
         * Array of settings saved globally, without distinction of user.
         */
        private val mGlobalSettingsKeys = Arrays.asList<String>()
    }


}

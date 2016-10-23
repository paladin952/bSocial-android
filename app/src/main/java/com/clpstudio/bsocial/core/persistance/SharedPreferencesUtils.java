package com.clpstudio.bsocial.core.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.clpstudio.bsocial.data.Session;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Concrete implementation for interacting with shared preferences
 */
public class SharedPreferencesUtils implements ISharedPreferencesUtils {

    /**
     * The shared preferences object
     */
    private SharedPreferences mGlobalSharedPreferences;

    /**
     * Array of settings saved globally, without distinction of user.
     */
    private final static List<String> mGlobalSettingsKeys = Arrays.asList();

    /**
     * Application's mContext
     */
    private Context mContext;

    private Session mSession;


    /**
     * The constructor
     *
     * @param mContext The application;s mContext
     */
    @Inject
    public SharedPreferencesUtils(Context mContext, Session session) {
        this.mContext = mContext;
        this.mSession = session;
        mGlobalSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public void registerOnChangeListener(final SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getUserPreferences().registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                listener.onSharedPreferenceChanged(sharedPreferences, key);
            }
        });

        mGlobalSharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                listener.onSharedPreferenceChanged(sharedPreferences, key);
            }
        });
    }

    @Override
    public void unregisterOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getUserPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        mGlobalSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Set the value of the preference
     *
     * @param key   The key
     * @param value The value
     */
    private void setPreferenceValue(String key, boolean value) {
        getPreferences(key).edit().putBoolean(key, value).apply();
    }

    /**
     * Set the value of the preference
     *
     * @param key   The key
     * @param value The value
     */
    private void setPreferenceValue(String key, String value) {
        getPreferences(key).edit().putString(key, value).apply();
    }

    /**
     * Set the value of the preference
     *
     * @param key   The key
     * @param value The value
     */
    private void setPreferenceValue(String key, int value) {
        getPreferences(key).edit().putInt(key, value).apply();
    }

    /**
     * Set the value of the preference
     *
     * @param key   The key
     * @param value The value
     */
    private void setPreferenceValue(String key, long value) {
        getPreferences(key).edit().putLong(key, value).apply();
    }

    /**
     * Set the value of the preference
     *
     * @param key The key
     */
    private boolean getPreferenceValue(String key, boolean defaultValue) {
        return getPreferences(key).getBoolean(key, defaultValue);
    }

    /**
     * get the value of the preference
     *
     * @param key The key
     */
    private int getPreferenceValue(String key, int defaultValue) {
        return getPreferences(key).getInt(key, defaultValue);
    }

    /**
     * get the value of the preference
     *
     * @param key The key
     */
    private long getPreferenceValue(String key, long defaultValue) {
        return getPreferences(key).getLong(key, defaultValue);
    }

    /**
     * get the value of the preference
     *
     * @param key The key
     */
    private String getPreferenceValue(String key, String defaultValue) {
        return getPreferences(key).getString(key, defaultValue);
    }

    /**
     * Returning preferences for the current logged in user
     *
     * @return The SharedPreferences
     */
    private SharedPreferences getUserPreferences() {
        return mContext.getSharedPreferences(getSharedPreferencesUserSpecificName(), Context.MODE_PRIVATE);
    }

    /**
     * Get the sharedPreferences based on the the key
     *
     * @param key The key
     * @return Global or UserSpecificPreferences based on the key
     */
    private SharedPreferences getPreferences(String key) {
        return mGlobalSettingsKeys.contains(key) ? mGlobalSharedPreferences : getUserPreferences();
    }

    /**
     * @return The name of the sharedPreferences for the current logged in user
     */
    public String getSharedPreferencesUserSpecificName() {
        return mContext.getPackageName() + SharedPreferencesConstants.INSTANCE.getUSER_SPECIFIC_FILE_NAME_PLACEHOLDER()
                + mSession.getUser().getId();
    }


}

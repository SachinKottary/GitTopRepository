package com.sachin.gogit.utils;

import android.content.SharedPreferences;

/**
 *  Manager class used for holding all the preference related operations
 */

public class GoGitAppPreferenceManager {

    private SharedPreferences sharedPreferences;
    private static final String GIT_REPO_LAST_UPDATE_TIME = "Git_Repo_Last_Update_Time";

    public GoGitAppPreferenceManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     *  Updates the last updated time after git top repo details has been downloaded
     * @param timeInMills time when git repo details updated
     */
    public void setGitRepoLastUpdateTime(long timeInMills) {
        sharedPreferences.edit()
                .putLong(GIT_REPO_LAST_UPDATE_TIME, timeInMills)
                .apply();
    }

    public long getLastGitRepoUpdatedTimeInMillis() {
        return sharedPreferences.getLong(GIT_REPO_LAST_UPDATE_TIME, 0L);
    }

}
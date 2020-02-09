package com.sachin.gogit.ui.main;

import com.sachin.gogit.utils.DateAndTimeUtils;
import com.sachin.gogit.utils.GoGitAppPreferenceManager;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.sachin.gogit.utils.DateAndTimeUtils.CACHE_EXPIRY_IN_MINUTES;

@Module
public class GoGitCacheHandler {

    private GoGitAppPreferenceManager preferenceManager;
    public GoGitCacheHandler(GoGitAppPreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public boolean isCacheTimeExpired() {
        return DateAndTimeUtils.isHourDifferenceGreaterThanTwo(System.currentTimeMillis(), preferenceManager.getLastGitRepoUpdatedTimeInMillis());
    }

    /**
     * Sets up a timer which will be executed after the minute difference for cache expire
     * @return
     */
    public Completable getTimerObservableForCacheExpiry() {
        int minutesForCacheExpire = CACHE_EXPIRY_IN_MINUTES - DateAndTimeUtils.getMinutesDifference(System.currentTimeMillis(), preferenceManager.getLastGitRepoUpdatedTimeInMillis());
        return Completable.timer(minutesForCacheExpire, TimeUnit.MINUTES, AndroidSchedulers.mainThread());
    }

}
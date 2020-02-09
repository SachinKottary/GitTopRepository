package com.sachin.gogit;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.sachin.gogit.dagger.components.DaggerGoGitApplicationComponent;
import com.sachin.gogit.dagger.components.GoGitApplicationComponent;
import com.sachin.gogit.dagger.modules.NetworkModule;
import com.sachin.gogit.dagger.modules.RealmDatabaseModule;
import com.sachin.gogit.dagger.modules.SharedPreferenceModule;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoGitApplication extends Application {

    private GoGitApplicationComponent applicationComponent;
    private static GoGitApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerGoGitApplicationComponent.builder()
                .networkModule(new NetworkModule())
                .realmDatabaseModule(new RealmDatabaseModule(getBaseContext()))
                .sharedPreferenceModule(new SharedPreferenceModule(getBaseContext()))
                .build();
        context = this;
    }

    public GoGitApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static GoGitApplication getApplication() {
        return context;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        clearGlideCacheMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_LOW || level == TRIM_MEMORY_RUNNING_CRITICAL || level == TRIM_MEMORY_UI_HIDDEN) {
            clearGlideCacheMemory();
        }
    }

    /**
     *  Free up some memory in case of low memory
     */
    public void clearGlideCacheMemory() {
        Completable.fromAction(() ->
                Glide.get(getApplication()).clearDiskCache())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Glide.get(getApplication()).clearMemory();
                    Glide.get(this).setMemoryCategory(MemoryCategory.LOW);
                });
    }

}
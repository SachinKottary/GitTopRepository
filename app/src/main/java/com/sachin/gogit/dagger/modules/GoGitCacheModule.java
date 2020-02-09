package com.sachin.gogit.dagger.modules;

import com.sachin.gogit.ui.main.GoGitCacheHandler;
import com.sachin.gogit.utils.GoGitAppPreferenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class GoGitCacheModule {

    @Provides
    public GoGitCacheHandler getCacheHandler(GoGitAppPreferenceManager preferenceManager) {
        return new GoGitCacheHandler(preferenceManager);
    }
}
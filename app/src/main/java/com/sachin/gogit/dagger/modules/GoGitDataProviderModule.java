package com.sachin.gogit.dagger.modules;

import com.sachin.gogit.database.RealmDatabaseManager;
import com.sachin.gogit.network.GoGitNetworkManager;
import com.sachin.gogit.ui.main.GoGitCacheHandler;
import com.sachin.gogit.ui.main.GoGitRepoDataProvider;
import com.sachin.gogit.utils.GoGitAppPreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GoGitDataProviderModule {

    @Provides
    @Singleton
    public GoGitRepoDataProvider getDataProvider(GoGitNetworkManager networkManager, RealmDatabaseManager databaseManager,
                                                 GoGitAppPreferenceManager preferenceManager, GoGitCacheHandler cacheHandler) {
        return new GoGitRepoDataProvider(networkManager, databaseManager, preferenceManager, cacheHandler);
    }
}
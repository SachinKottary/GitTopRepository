package com.sachin.gogit.dagger.components;

import com.sachin.gogit.dagger.modules.GoGitCacheModule;
import com.sachin.gogit.dagger.modules.GoGitDataProviderModule;
import com.sachin.gogit.dagger.modules.NetworkModule;
import com.sachin.gogit.dagger.modules.RealmDatabaseModule;
import com.sachin.gogit.dagger.modules.SharedPreferenceModule;
import com.sachin.gogit.dagger.modules.ViewModelModule;
import com.sachin.gogit.ui.main.GoGitRepositoryFragment;
import com.sachin.gogit.ui.main.GoGitRepositoryViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 *  Dagger single app component used for injecting SharedPreferenceModule.class, NetworkModule.class, RealmDatabaseModule.class
 */
@Singleton
@Component(modules = {SharedPreferenceModule.class, NetworkModule.class, RealmDatabaseModule.class, ViewModelModule.class,
        GoGitDataProviderModule.class, GoGitCacheModule.class})
public interface GoGitApplicationComponent {

    void inject(GoGitRepositoryViewModel viewModel);

    void inject(GoGitRepositoryFragment fragment);

}
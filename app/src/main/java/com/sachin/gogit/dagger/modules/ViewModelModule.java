package com.sachin.gogit.dagger.modules;

import androidx.lifecycle.ViewModel;

import com.sachin.gogit.annotation.ViewModelKey;
import com.sachin.gogit.ui.main.GoGitRepositoryViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GoGitRepositoryViewModel.class)
    abstract ViewModel bindUserViewModel(GoGitRepositoryViewModel userViewModel);

}
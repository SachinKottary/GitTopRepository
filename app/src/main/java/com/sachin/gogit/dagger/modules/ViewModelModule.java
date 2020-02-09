package com.sachin.gogit.dagger.modules;

import androidx.lifecycle.ViewModel;

import com.sachin.gogit.base.ViewModelFactory;
import com.sachin.gogit.ui.main.GoGitRepoDataProvider;
import com.sachin.gogit.ui.main.GoGitRepositoryViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }

    @Provides
    @IntoMap
    @ViewModelKey(GoGitRepositoryViewModel.class)
    ViewModel viewModel1(GoGitRepoDataProvider fetchDataUseCase1) {
        return new GoGitRepositoryViewModel(fetchDataUseCase1);
    }
}
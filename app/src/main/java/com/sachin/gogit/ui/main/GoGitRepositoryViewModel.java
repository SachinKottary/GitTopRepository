package com.sachin.gogit.ui.main;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sachin.gogit.network.dto.GitTopRepositoryDetails;

import java.util.List;

import javax.inject.Inject;

public class GoGitRepositoryViewModel extends ViewModel {

    private GoGitRepoDataProvider goGitRepoDataProvider;
    public ObservableBoolean isDataLoading;
    public MutableLiveData<List<GitTopRepositoryDetails>> gitRepoListLiveData;
    public MutableLiveData<String> gitRepoListErrorLiveData;

    public GoGitRepositoryViewModel() {}

    @Inject
    public GoGitRepositoryViewModel(GoGitRepoDataProvider dataProvider) {
        goGitRepoDataProvider = dataProvider;
        isDataLoading = dataProvider.isDataLoading;
        gitRepoListLiveData = dataProvider.gitRepoListLiveData;
        gitRepoListErrorLiveData = dataProvider.gitRepoListErrorLiveData;
        getTopRepositoryDetails();
    }

    @Override
    public void onCleared() {
        super.onCleared();
        if (goGitRepoDataProvider == null) return;
        goGitRepoDataProvider.onClear();
    }

    public void getTopRepositoryDetails() {
        if (goGitRepoDataProvider == null) return;
        goGitRepoDataProvider.getTopRepositoryDetails();
    }

    public void onSwipeRefresh() {
        downloadTopGitRepoFromServer(true);
    }

    public void downloadTopGitRepoFromServer(boolean isSwipeRefresh) {
        goGitRepoDataProvider.downloadTopGitRepoFromServer(isSwipeRefresh);
    }

}

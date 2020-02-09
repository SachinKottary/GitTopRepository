package com.sachin.gogit.ui.main;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.sachin.gogit.GoGitApplication;
import com.sachin.gogit.database.RealmDatabaseManager;
import com.sachin.gogit.network.GoGitNetworkManager;
import com.sachin.gogit.network.dto.GitTopRepositoryDetails;
import com.sachin.gogit.utils.GoGitAppPreferenceManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class GoGitRepoDataProvider {

    private GoGitNetworkManager goGitNetworkManager;
    private RealmDatabaseManager databaseManager;
    private GoGitAppPreferenceManager appPreferenceManager;
    private GoGitCacheHandler cacheHandler;

    public ObservableBoolean isDataLoading = new ObservableBoolean();
    public MutableLiveData<List<GitTopRepositoryDetails>> gitRepoListLiveData = new MutableLiveData<>();
    public MutableLiveData<String> gitRepoListErrorLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public GoGitRepoDataProvider(GoGitNetworkManager networkManager, RealmDatabaseManager databaseManager,
                                    GoGitAppPreferenceManager preferenceManager, GoGitCacheHandler cacheHandler) {
        goGitNetworkManager = networkManager;
        this.databaseManager = databaseManager;
        appPreferenceManager = preferenceManager;
        this.cacheHandler = cacheHandler;
    }

    public void getTopRepositoryDetails() {
        if (databaseManager == null) return;
        List<GitTopRepositoryDetails> gitCachedDetailList = databaseManager.getCachedGitTopRepoDetailList();
        //If there is no cached data or if cache time expired, then download new data from server
        if (gitCachedDetailList == null || gitCachedDetailList.isEmpty() || cacheHandler.isCacheTimeExpired()) {
            GoGitApplication.getApplication().clearGlideCacheMemory();
            downloadTopGitRepoFromServer(false);
        } else {
            loadGitTopRepoDetails(gitCachedDetailList);
            setupTimerForCacheExpire();
        }
    }

    /**
     * Notify the passed repo details to the view
     *
     * @param gitCachedDetailList data to be updated in view
     */
    private void loadGitTopRepoDetails(List<GitTopRepositoryDetails> gitCachedDetailList) {
        gitRepoListLiveData.setValue(gitCachedDetailList);
    }

    /**
     * This method downloads top git repo details from the server
     */
    public void downloadTopGitRepoFromServer(boolean isSwipeRefresh) {
        isDataLoading.set(isSwipeRefresh);
        goGitNetworkManager.getGitTopRepoDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiCallbackObserver);
    }


    /**
     * This method deletes old git repo data and inserts the passed new data to realm database
     *
     * @param detailsList Data to be added to database
     */
    private void updateGitRepoDetailsInDataBase(List<GitTopRepositoryDetails> detailsList) {
        if (databaseManager == null || detailsList == null) return;
        databaseManager.deleteAllGitRepoDetails();
        databaseManager.insertGitTopRepositoryDetailsToRealm(detailsList);
    }

    /**
     * Sets up a timer which will be executed after the minute difference for cache expire
     */
    private void setupTimerForCacheExpire() {
        compositeDisposable.add(cacheHandler.getTimerObservableForCacheExpiry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(() -> downloadTopGitRepoFromServer(true)));
    }

    public void onClear() {
        if (compositeDisposable == null || !compositeDisposable.isDisposed()) return;
        compositeDisposable.dispose();
    }


    private Observer<List<GitTopRepositoryDetails>> apiCallbackObserver = new Observer<List<GitTopRepositoryDetails>>() {
        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(List<GitTopRepositoryDetails> detailsList) {
            updateGitRepoDetailsInDataBase(detailsList);
            isDataLoading.set(false);
            if (detailsList == null || detailsList.isEmpty()) return;
            //Notify view about the new data
            gitRepoListLiveData.setValue(detailsList);
            //Save the time in preference to check for cache expire
            appPreferenceManager.setGitRepoLastUpdateTime(System.currentTimeMillis());
        }

        @Override
        public void onError(Throwable e) {
            isDataLoading.set(false);
            //Notify view about the new data
            gitRepoListErrorLiveData.setValue(null);
        }

        @Override
        public void onComplete() {
            isDataLoading.set(false);
        }
    };

}
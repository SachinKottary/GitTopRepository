package com.sachin.gogit.ui.main;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sachin.gogit.GoGitApplication;
import com.sachin.gogit.database.RealmDatabaseManager;
import com.sachin.gogit.network.GoGitNetworkManager;
import com.sachin.gogit.network.dto.GitTopRepositoryDetails;
import com.sachin.gogit.utils.DateAndTimeUtils;
import com.sachin.gogit.utils.GoGitAppPreferenceManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.sachin.gogit.utils.DateAndTimeUtils.CACHE_EXPIRY_IN_MINUTES;

public class GoGitRepositoryViewModel extends ViewModel {

    private GoGitNetworkManager goGitNetworkManager;
    private RealmDatabaseManager databaseManager;
    private GoGitAppPreferenceManager appPreferenceManager;

    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<List<GitTopRepositoryDetails>> gitRepoListLiveData = new MutableLiveData<>();
    private MutableLiveData<String> gitRepoListErrorLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public GoGitRepositoryViewModel(GoGitNetworkManager networkManager, RealmDatabaseManager databaseManager,
                                    GoGitAppPreferenceManager preferenceManager) {

        getTopRepositoryDetails();
    }

    @Override
    public void onCleared() {
        super.onCleared();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    MutableLiveData<List<GitTopRepositoryDetails>> getGitRepoListLiveData() {
        return gitRepoListLiveData;
    }

    MutableLiveData<String> getGitRepoListErrorLiveData() {
        return gitRepoListErrorLiveData;
    }

    public void getTopRepositoryDetails() {
        if (databaseManager == null) return;
        List<GitTopRepositoryDetails> gitCachedDetailList = databaseManager.getCachedGitTopRepoDetailList();
        //If there is no cached data or if cache time expired, then download new data from server
        if (gitCachedDetailList == null || gitCachedDetailList.isEmpty() || isCacheTimeExpired()) {
            GoGitApplication.getApplication().clearGlideCacheMemory();
            downloadTopGitRepoFromServer();
        } else {
            loadGitTopRepoDetails(gitCachedDetailList);
            setupTimerForCacheExpire();
        }
    }

    /**
     * Sets up a timer which will be executed after the minute difference for cache expire
     */
    private void setupTimerForCacheExpire() {
        int minutesForCacheExpire = CACHE_EXPIRY_IN_MINUTES - DateAndTimeUtils.getMinutesDifference(System.currentTimeMillis(), appPreferenceManager.getLastGitRepoUpdatedTimeInMillis());
        compositeDisposable.add(Completable.timer(minutesForCacheExpire, TimeUnit.MINUTES, AndroidSchedulers.mainThread())
                .subscribe(this::downloadTopGitRepoFromServer));
    }

    /**
     *  Notify the passed repo details to the view
     * @param gitCachedDetailList data to be updated in view
     */
    private void loadGitTopRepoDetails(List<GitTopRepositoryDetails> gitCachedDetailList) {
        getGitRepoListLiveData().setValue(gitCachedDetailList);
    }

    private boolean isCacheTimeExpired() {
        return DateAndTimeUtils.isHourDifferenceGreaterThanTwo(System.currentTimeMillis(), appPreferenceManager.getLastGitRepoUpdatedTimeInMillis());
    }

    /**
     *  This method downloads top git repo details from the server
     */
    public void downloadTopGitRepoFromServer() {
        goGitNetworkManager.getGitTopRepoDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiCallbackObserver);
    }

    /**
     *  This method deletes old git repo data and inserts the passed new data to realm database
     * @param detailsList Data to be added to database
     */
    private void updateGitRepoDetailsInDataBase(List<GitTopRepositoryDetails> detailsList) {
        if (databaseManager == null || detailsList == null) return;
        databaseManager.deleteAllGitRepoDetails();
        databaseManager.insertGitTopRepositoryDetailsToRealm(detailsList);
    }

    public void onSwipeRefresh() {
        isDataLoading.setValue(true);
        downloadTopGitRepoFromServer();
    }

    private Observer<List<GitTopRepositoryDetails>> apiCallbackObserver = new Observer<List<GitTopRepositoryDetails>>() {
        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(List<GitTopRepositoryDetails> detailsList) {
            updateGitRepoDetailsInDataBase(detailsList);
            //Notify view about the new data
            gitRepoListLiveData.setValue(detailsList);
            //Save the time in preference to check for cache expire
            appPreferenceManager.setGitRepoLastUpdateTime(System.currentTimeMillis());
        }

        @Override
        public void onError(Throwable e) {
            isDataLoading.setValue(false);
            //Notify view about the new data
            gitRepoListLiveData.setValue(null);
        }

        @Override
        public void onComplete() {
            isDataLoading.setValue(false);
        }
    };


}

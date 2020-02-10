package com.sachin.gogit.ui;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.sachin.gogit.GoGitApplication;
import com.sachin.gogit.database.RealmDatabaseManager;
import com.sachin.gogit.database.dto.GitTopRepositoryDatabaseDetails;
import com.sachin.gogit.network.GoGitNetworkManager;
import com.sachin.gogit.network.dto.GitTopRepositoryDetails;
import com.sachin.gogit.ui.main.GoGitCacheHandler;
import com.sachin.gogit.ui.main.GoGitRepoDataProvider;
import com.sachin.gogit.ui.main.GoGitRepositoryViewModel;
import com.sachin.gogit.utils.DateAndTimeUtils;
import com.sachin.gogit.utils.DateAndTimeUtilsTest;
import com.sachin.gogit.utils.GoGitAppPreferenceManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class GoGitRepoDataProviderTest {

    @Mock
    private GoGitNetworkManager goGitNetworkManagerMock;
    @Mock
    private RealmDatabaseManager databaseManagerMock;
    @Mock
    private GoGitAppPreferenceManager appPreferenceManagerMock;
    @Mock
    private GoGitCacheHandler cacheHandlerMock;
    @Mock
    GoGitApplication application;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GoGitRepoDataProvider dataProvider;

    private Observable<List<GitTopRepositoryDetails>> gitTopRepoObservable = Observable.create(e -> {
                e.onNext(new ArrayList<>());
                e.onComplete();
            }
    );

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        application.onCreate();
        dataProvider = new GoGitRepoDataProvider(goGitNetworkManagerMock, databaseManagerMock,appPreferenceManagerMock,cacheHandlerMock);
        when(goGitNetworkManagerMock.getGitTopRepoDetails()).thenReturn(gitTopRepoObservable);
    }

    @Test
    public void test() {
        Assert.assertTrue(true);
    }

    @Test
    public void checkApiCalledWhenDatabaseIsNull() {
        when(databaseManagerMock.getCachedGitTopRepoDetailList()).thenReturn(null);
        dataProvider.getTopRepositoryDetails();
        Mockito.verify(goGitNetworkManagerMock, Mockito.atLeastOnce()).getGitTopRepoDetails();
    }

    @Test
    public void checkApiCalledWhenDatabaseIsEmpty() {
        when(databaseManagerMock.getCachedGitTopRepoDetailList()).thenReturn(new ArrayList<>());
        dataProvider.getTopRepositoryDetails();
        Mockito.verify(goGitNetworkManagerMock, Mockito.atLeastOnce()).getGitTopRepoDetails();
    }

    @Test
    public void checkIfCacheExpiryCalledWhenDataBaseIsPresent() {
        List<GitTopRepositoryDetails> detailsList = new ArrayList<>();
        detailsList.add(new GitTopRepositoryDetails());
        Completable completable = Completable.create(CompletableEmitter::onComplete);
        when(databaseManagerMock.getCachedGitTopRepoDetailList()).thenReturn(detailsList);
        when(cacheHandlerMock.getTimerObservableForCacheExpiry()).thenReturn(completable);
        dataProvider.getTopRepositoryDetails();
        Mockito.verify(cacheHandlerMock, Mockito.atLeastOnce()).isCacheTimeExpired();
    }

    @Test
    public void checkCacheExpiryIsSet() {
        List<GitTopRepositoryDetails> detailsList = new ArrayList<>();
        detailsList.add(new GitTopRepositoryDetails());
        Completable completable = Completable.create(CompletableEmitter::onComplete);
        when(databaseManagerMock.getCachedGitTopRepoDetailList()).thenReturn(detailsList);
        when(cacheHandlerMock.getTimerObservableForCacheExpiry()).thenReturn(completable);
        dataProvider.getTopRepositoryDetails();
        Mockito.verify(cacheHandlerMock, Mockito.atLeastOnce()).getTimerObservableForCacheExpiry();
    }

    @Test
    public void checkIfApiIsCalledWhenCacheIsExpired() {
        List<GitTopRepositoryDetails> detailsList = new ArrayList<>();
        detailsList.add(new GitTopRepositoryDetails());
        Completable completable = Completable.create(CompletableEmitter::onComplete);
        when(databaseManagerMock.getCachedGitTopRepoDetailList()).thenReturn(detailsList);
        when(cacheHandlerMock.getTimerObservableForCacheExpiry()).thenReturn(completable);
        when(cacheHandlerMock.isCacheTimeExpired()).thenReturn(true);
        dataProvider.getTopRepositoryDetails();
        Mockito.verify(goGitNetworkManagerMock, Mockito.atLeastOnce()).getGitTopRepoDetails();
    }

    @Test//For method isCacheTimeExpired()
    public void checkIfCacheTimeExpiredCorrectly() {
        DateAndTimeUtilsTest dateAndTimeUtilsTest = new DateAndTimeUtilsTest();
        long oldTime = 1111111111L;
        assertTrue(dateAndTimeUtilsTest.isHourDifferenceGreaterThanTwo(System.currentTimeMillis(), oldTime));
    }

    @Test//For method isCacheTimeExpired()
    public void checkIfCacheTimeExpiredCorrectlyForFalseCase() {
        DateAndTimeUtilsTest dateAndTimeUtilsTest = new DateAndTimeUtilsTest();
        long newTime = 1111111111L;
        assertFalse(dateAndTimeUtilsTest.isHourDifferenceGreaterThanTwo(newTime, System.currentTimeMillis()));
    }

}

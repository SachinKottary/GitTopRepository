package com.sachin.gogit;

import com.sachin.gogit.ui.main.GoGitRepoDataProvider;
import com.sachin.gogit.ui.main.GoGitRepositoryViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class GoGitRepositoryViewModelTest {

    @Mock
    GoGitRepoDataProvider dataProviderMock;
    GoGitRepositoryViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new GoGitRepositoryViewModel(dataProviderMock);
    }

    @Test
    public void checkIfGetTopRepositoryDetailsApiCalled() {
        Mockito.verify(dataProviderMock, Mockito.atLeastOnce()).getTopRepositoryDetails();
    }

    @Test
    public void checkIfOnClearMethodCalled() {
        viewModel.onCleared();
        Mockito.verify(dataProviderMock, Mockito.atLeastOnce()).onClear();
    }

    @Test
    public void checkIfApiCalledOnSwipeRefresh() {
        viewModel.onSwipeRefresh();
        Mockito.verify(dataProviderMock, Mockito.atLeastOnce()).downloadTopGitRepoFromServer(true);
    }

    @Test
    public void checkIfApiCalledOnSwipeRefreshWithFalse() {
        viewModel.downloadTopGitRepoFromServer(false);
        Mockito.verify(dataProviderMock, Mockito.atLeastOnce()).downloadTopGitRepoFromServer(false);
    }
}
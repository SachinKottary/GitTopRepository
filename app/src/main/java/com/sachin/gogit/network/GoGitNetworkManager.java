package com.sachin.gogit.network;

import com.sachin.gogit.network.dto.GitTopRepositoryDetails;
import com.sachin.gogit.utils.NetworkUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Helper class to handle network related operations
 */

public class GoGitNetworkManager {

    private RetrofitApiInterface retrofitApiInterface;

    public GoGitNetworkManager(RetrofitApiInterface RetrofitInterface) {
        retrofitApiInterface = RetrofitInterface;
    }

    /**
     *  Fetches api response for delivery details
     * @param callback rx subscriber to notify success or error
     * @return
     */
    public Observable<List<GitTopRepositoryDetails>> getGitTopRepoDetails() {
        return retrofitApiInterface.getGitTopRepoDetails(NetworkUtils.getUrl(ServerConstants.REQUEST_GET_TOP_GIT_REPOS));
    }

}
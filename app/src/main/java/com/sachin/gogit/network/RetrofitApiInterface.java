package com.sachin.gogit.network;

import com.sachin.gogit.network.dto.GitTopRepositoryDetails;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitApiInterface {

    @GET()
    Observable<List<GitTopRepositoryDetails>> getGitTopRepoDetails(@Url String url);

}
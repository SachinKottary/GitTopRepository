package com.sachin.gogit.interfaces;

import android.view.View;

import com.sachin.gogit.network.dto.GitTopRepositoryDetails;

public interface GoGitRepoDetailClickListener {

    void onGitRepoDetailClicked(View view, GitTopRepositoryDetails gitTopRepositoryDetails);

}
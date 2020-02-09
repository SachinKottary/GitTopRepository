package com.sachin.gogit.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sachin.gogit.GoGitApplication;
import com.sachin.gogit.R;
import com.sachin.gogit.base.GoGitBaseFragment;
import com.sachin.gogit.base.ViewModelFactory;
import com.sachin.gogit.databinding.FragmentGitRepoListBinding;
import com.sachin.gogit.ui.main.adapter.GoGitRepositoryAdapter;

import javax.inject.Inject;

public class GoGitRepositoryFragment extends GoGitBaseFragment  {
    @Inject
    ViewModelFactory viewModelFactory;
    private GoGitRepositoryViewModel viewModel;
    private GoGitRepositoryAdapter gitRepositoryAdapter;
    private FragmentGitRepoListBinding binding;

    private ImageView networkErrorImage;
    private Button retryButton;
    private ProgressBar progressBar;

    public static GoGitRepositoryFragment newInstance() {
        return new GoGitRepositoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        GoGitApplication.getApplication().getApplicationComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_git_repo_list, container, false);
        gitRepositoryAdapter = new GoGitRepositoryAdapter();
        networkErrorImage = binding.networkErrorImage;
        retryButton = binding.retryButton;
        progressBar = binding.progressBar;
        binding.gitRepoRecyclerView.setAdapter(gitRepositoryAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GoGitRepositoryViewModel.class);
        binding.setGitRepoViewModel(viewModel);
        observeGitTopRepoDetails();
    }

    private void observeGitTopRepoDetails() {
        viewModel.gitRepoListLiveData.observe(this, detailsList -> {
            if (gitRepositoryAdapter == null || progressBar == null) return;
            progressBar.setVisibility(View.GONE);
            gitRepositoryAdapter.setGitTopRepoDetails(detailsList);
        });

        viewModel.gitRepoListErrorLiveData.observe(this, detailsList -> {
            if (detailsList == null || detailsList.isEmpty()) {
                onNetworkDisConnected();
                return;
            }
            if (progressBar == null) return;
            progressBar.setVisibility(View.GONE);
        });
    }

    private void getGitTopRepoDetails() {
        viewModel.getTopRepositoryDetails();
    }

    @Override
    public boolean handleNetworkState() {
        return true;
    }

    @Override
    public void onNetworkDisConnected() {
        if (gitRepositoryAdapter == null) return;
        int networkErrorVisibility = gitRepositoryAdapter.getItemCount() <= 0 ? View.VISIBLE : View.GONE;
        networkErrorImage.setVisibility(networkErrorVisibility);
        retryButton.setVisibility(networkErrorVisibility);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onNetworkConnected() {
        if (gitRepositoryAdapter == null || gitRepositoryAdapter.getItemCount() > 0) return;
        networkErrorImage.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        getGitTopRepoDetails();
    }

}

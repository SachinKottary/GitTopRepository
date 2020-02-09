package com.sachin.gogit.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sachin.gogit.R;
import com.sachin.gogit.base.GoGitBaseFragment;

public class GoGitRepositoryFragment extends GoGitBaseFragment {

    private GoGitRepositoryViewModel mViewModel;

    public static GoGitRepositoryFragment newInstance() {
        return new GoGitRepositoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GoGitRepositoryViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public boolean handleNetworkState() {
        return true;
    }

    @Override
    public void onNetworkDisConnected() {

    }

    @Override
    public void onNetworkConnected() {

    }
}

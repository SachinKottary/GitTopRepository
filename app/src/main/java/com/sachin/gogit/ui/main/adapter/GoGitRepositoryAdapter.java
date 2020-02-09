package com.sachin.gogit.ui.main.adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.gogit.R;
import com.sachin.gogit.databinding.GitRepoListItemBinding;
import com.sachin.gogit.network.dto.GitTopRepositoryDetails;
import com.sachin.gogit.ui.main.viewholders.GoGitRepositoryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GoGitRepositoryAdapter extends RecyclerView.Adapter<GoGitRepositoryViewHolder> {
    private List<GitTopRepositoryDetails> gitTopRepositoryDetailsList = new ArrayList<>();
    private int expandPosition = -1;

    public GoGitRepositoryAdapter() {
    }

    @NonNull
    @Override
    public GoGitRepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GitRepoListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.git_repo_list_item,
                        parent, false);
        return new GoGitRepositoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GoGitRepositoryViewHolder holder, int position) {
        GitTopRepositoryDetails gitTopRepositoryDetails = gitTopRepositoryDetailsList.get(position);
        holder.gitRepoListItemBinding.setGitDetails(gitTopRepositoryDetails);
        holder.gitRepoListItemBinding.executePendingBindings();

        GitTopRepositoryDetails expandDetail = gitTopRepositoryDetailsList.get(position);
        holder.onBind(expandDetail);
        holder.gitRepoListItemBinding.setClickListener((view, gitTopRepositoryDetails1) -> {
            int expandPosition = getExpandedPosition();
            if (expandPosition != -1 && expandPosition != position) {
                GitTopRepositoryDetails oldExpandDetail = gitTopRepositoryDetailsList.get(expandPosition);
                oldExpandDetail.setExpanded(false);
                notifyItemChanged(expandPosition);
            }
            this.expandPosition = position;
            expandDetail.setExpanded(!expandDetail.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return gitTopRepositoryDetailsList.size();
    }

    public void setGitTopRepoDetails(List<GitTopRepositoryDetails> gitTopRepoDetails) {
        if (gitTopRepoDetails == null) return;
        gitTopRepositoryDetailsList.clear();
        gitTopRepositoryDetailsList.addAll(gitTopRepoDetails);
        notifyDataSetChanged();
    }

    private int getExpandedPosition() {
        if (expandPosition >= 0) return expandPosition;
        if (gitTopRepositoryDetailsList == null || gitTopRepositoryDetailsList.isEmpty()) return -1;
        for (int i=0; i<gitTopRepositoryDetailsList.size(); i++) {
            if (gitTopRepositoryDetailsList.get(i).isExpanded()) return i;
        }
        return -1;
    }


}
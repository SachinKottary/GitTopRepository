package com.sachin.gogit.ui.main.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.gogit.R;
import com.sachin.gogit.databinding.GitRepoListItemBinding;
import com.sachin.gogit.network.dto.GitTopRepositoryDetails;

import java.util.HashMap;

/**
 *  Used as view holder for git repository details
 */
public class GoGitRepositoryViewHolder extends RecyclerView.ViewHolder {

    public GitRepoListItemBinding gitRepoListItemBinding;
    public static HashMap<String, Drawable> languageColorMap = new HashMap<>();

    public GoGitRepositoryViewHolder(@NonNull GitRepoListItemBinding itemView) {
        super(itemView.getRoot());
        gitRepoListItemBinding = itemView;
    }

    public void onBind(GitTopRepositoryDetails details) {
        if (TextUtils.isEmpty(details.getLanguage())) return;
        if (languageColorMap.get(details.getLanguage()) == null) {
            Context context = gitRepoListItemBinding.getRoot().getContext();
            Drawable drawable = context.getResources().getDrawable(R.drawable.circle_shape);
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(Color.parseColor(details.getLanguageColor()));
            gitRepoListItemBinding.languageColorImage.setImageDrawable(drawable);
            languageColorMap.put(details.getLanguage(),drawable);
        } else {
            gitRepoListItemBinding.languageColorImage.setImageDrawable(languageColorMap.get(details.getLanguage()));
        }
    }

}
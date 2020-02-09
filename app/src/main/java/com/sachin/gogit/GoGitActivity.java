package com.sachin.gogit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sachin.gogit.base.GoGitBaseActivity;
import com.sachin.gogit.ui.main.GoGitRepositoryFragment;

public class GoGitActivity extends GoGitBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, GoGitRepositoryFragment.newInstance())
                    .commitNow();
        }
    }
}

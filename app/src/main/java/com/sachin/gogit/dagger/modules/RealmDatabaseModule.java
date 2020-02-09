package com.sachin.gogit.dagger.modules;

import android.content.Context;

import com.sachin.gogit.database.RealmDatabaseManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *  Used for providing database related dependency
 */

@Module
public class RealmDatabaseModule {

    private Context mContext;

    public RealmDatabaseModule(Context context) {
        mContext = context;
    }

    @Provides
    public RealmConfiguration getRealMConfiguration() {
        return new RealmConfiguration.Builder(mContext)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    @Provides
    @Singleton
    public Realm getRealm(RealmConfiguration realmConfiguration) {
        Realm.setDefaultConfiguration(realmConfiguration);
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    public RealmDatabaseManager getRealmController(Realm realm) {
        return new RealmDatabaseManager(realm);
    }

}


package com.sachin.gogit.database;
import io.realm.Realm;
/**
 *  Used to provide access to realm database to save, update or delete data
 */
public class RealmDatabaseManager {

    private Realm realmDatabase;

    public RealmDatabaseManager(Realm realm) {
        realmDatabase = realm;
    }

}

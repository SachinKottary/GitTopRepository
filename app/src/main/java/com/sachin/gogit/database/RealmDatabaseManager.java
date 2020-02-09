package com.sachin.gogit.database;
import com.sachin.gogit.database.dto.GitTopRepositoryDatabaseDetails;
import com.sachin.gogit.network.dto.GitTopRepositoryDetails;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 *  Used to provide access to realm database to save, update or delete data
 */
public class RealmDatabaseManager {
    private Realm realmDatabase;

    public RealmDatabaseManager(Realm realm) {
        realmDatabase = realm;
    }

    public void insertGitTopRepositoryDetailsToRealm(List<GitTopRepositoryDetails> detailsList) {
        if (detailsList == null || detailsList.isEmpty()) return;
        List<GitTopRepositoryDatabaseDetails> databaseDetailsList = new ArrayList<>();
        for (int iterator = 0; iterator < detailsList.size(); iterator++) {

            GitTopRepositoryDetails gitTopRepositoryDetails = detailsList.get(iterator);
            GitTopRepositoryDatabaseDetails databaseDetails = new GitTopRepositoryDatabaseDetails();

            databaseDetails.setId(iterator);
            databaseDetails.setAuthor(gitTopRepositoryDetails.getAuthor());
            databaseDetails.setName(gitTopRepositoryDetails.getName());
            databaseDetails.setAvatar(gitTopRepositoryDetails.getAvatar());
            databaseDetails.setDescription(gitTopRepositoryDetails.getDescription());
            databaseDetails.setLanguage(gitTopRepositoryDetails.getLanguage());
            databaseDetails.setLanguageColor(gitTopRepositoryDetails.getLanguageColor());
            databaseDetails.setForks(gitTopRepositoryDetails.getForks());
            databaseDetails.setStars(gitTopRepositoryDetails.getStars());

            databaseDetailsList.add(databaseDetails);
        }
        if (databaseDetailsList.isEmpty()) return;
        realmDatabase.beginTransaction();
        realmDatabase.copyToRealm(databaseDetailsList);
        realmDatabase.commitTransaction();
    }

    private RealmQuery<GitTopRepositoryDatabaseDetails> getTopRepoDetailList() {
        return realmDatabase.where(GitTopRepositoryDatabaseDetails.class);
    }

    public List<GitTopRepositoryDetails> getCachedGitTopRepoDetailList() {
        RealmQuery<GitTopRepositoryDatabaseDetails> databaseDetailsRealmQuery = getTopRepoDetailList();
        if (databaseDetailsRealmQuery == null) return null;
        RealmResults<GitTopRepositoryDatabaseDetails> resultList = databaseDetailsRealmQuery.findAll();
        if (resultList == null || resultList.isEmpty()) return null;
        List<GitTopRepositoryDetails> gitTopRepositoryDetailsList = new ArrayList<>();
        for (int iterator = 0; iterator < resultList.size(); iterator++) {

            GitTopRepositoryDetails gitTopRepositoryDetails = new GitTopRepositoryDetails();
            GitTopRepositoryDatabaseDetails gitTopRepositoryDatabaseDetails = resultList.get(iterator);

            gitTopRepositoryDetails.setName(gitTopRepositoryDatabaseDetails.getName());
            gitTopRepositoryDetails.setAuthor(gitTopRepositoryDatabaseDetails.getAuthor());
            gitTopRepositoryDetails.setAvatar(gitTopRepositoryDatabaseDetails.getAvatar());
            gitTopRepositoryDetails.setDescription(gitTopRepositoryDatabaseDetails.getDescription());
            gitTopRepositoryDetails.setStars(gitTopRepositoryDatabaseDetails.getStars());
            gitTopRepositoryDetails.setForks(gitTopRepositoryDatabaseDetails.getForks());
            gitTopRepositoryDetails.setLanguageColor(gitTopRepositoryDatabaseDetails.getLanguageColor());
            gitTopRepositoryDetails.setLanguage(gitTopRepositoryDatabaseDetails.getLanguage());
            gitTopRepositoryDetailsList.add(gitTopRepositoryDetails);
        }
        return gitTopRepositoryDetailsList;
    }

    /**
     *  This method is used for deleting previous data in the database
     */
    public void deleteAllGitRepoDetails() {
        if (realmDatabase == null) return;
        realmDatabase.beginTransaction();
        realmDatabase.clear(GitTopRepositoryDatabaseDetails.class);
        realmDatabase.commitTransaction();
    }

}

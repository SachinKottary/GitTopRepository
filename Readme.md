GoGit Android Application

An Android application used for fetching the top git repository and display it to the user.

Language Used: JAVA

Architecture Design Pattern: MVVM

Behaviour Pattern: Singleton, Dependency Injection, Reactive and Observer.

Third party SDK: Dagger 2, RxJava 2, Retrofit, Glide and Realm.

Testing SDK: Junit, Mockito and Expresso.

Other details:
1. Handled automatic data loading when connected to network if data is not present.
2. Realm database used for offline caching.
3. Orientation support and data retention using View Model.
4. Retrofit combined with RxJava.
5. Autochecking for cache expiry for every 2 hours, and also sets up a Rx observer as a schedular at the app launch if cache period is valid.
6. Swipe in refresh layout for manual loading of data.
7. Used Glide SDK for downloading and disk caching.
8. Handled low memory scenarios to clear Glide memory.
9. Provided Ripple effect for the views.
10. Used Dagger 2 to break up codes in to separate modules.
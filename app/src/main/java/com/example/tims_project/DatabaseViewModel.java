package com.example.tims_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;

public class DatabaseViewModel extends ViewModel {
    private FirebaseInstanceDatabase instance;

    public LiveData<DatabaseReference> getTokenRefDb;




    public DatabaseViewModel() {
        instance = new FirebaseInstanceDatabase();
    }






    public void getTokenDatabaseRef(){
        getTokenRefDb = instance.getTokenRef();
    }


}


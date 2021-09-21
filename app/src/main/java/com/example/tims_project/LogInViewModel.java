package com.example.tims_project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInViewModel extends ViewModel {
    private FirebaseLoginInstance loginInstance;

    public LiveData<Boolean> successUpdateToken;




    public void updateToken(String newToken) {
        successUpdateToken = loginInstance.successUpdateToken(newToken);
    }

}


package com.example.elmira_vaccine.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.elmira_vaccine.models.Nurse;
import com.example.elmira_vaccine.reposiotories.NurseRepository;

import java.util.List;

public class NurseViewModel extends AndroidViewModel {

    private final NurseRepository repository = new NurseRepository();
    private static NurseViewModel instance;
    public MutableLiveData<List<Nurse>> allNurses;


    public NurseViewModel(@NonNull Application application) {
        super(application);
    }
    public static NurseViewModel getInstance(Application application){
        if(instance == null){
            instance = new NurseViewModel(application);
        }
        return instance;
    }
    public NurseRepository getRepository() {
        return this.repository;
    }

    public void getAllNurse(){
        this.repository.getAllNurse();
        this.allNurses = this.repository.allNurses;
    }
}
package com.example.elmira_vaccine.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.elmira_vaccine.models.Nurse;
import com.example.elmira_vaccine.models.Patient;
import com.example.elmira_vaccine.reposiotories.PatientRepository;

import java.util.List;

public class PatientViewModel extends AndroidViewModel {

    private final PatientRepository repository = new PatientRepository();
    private static PatientViewModel instance;
    public MutableLiveData<List<Patient>> allPatients;


    public PatientViewModel(@NonNull Application application) {
        super(application);
    }
    public static PatientViewModel getInstance(Application application){
        if(instance == null){
            instance = new PatientViewModel(application);
        }
        return instance;
    }
    public PatientRepository getRepository(){
        return  this.repository;
    }
    public void addPatient(Patient patient){
        this.repository.addPatient(patient);
    }
    public void updatePatient(Patient patient){
        this.repository.updatePatient(patient);
    }


    public void getPatient(String h){
        this.repository.getPatient(h);
        this.allPatients = this.repository.allPatients;
    }
}

package com.example.elmira_vaccine.reposiotories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.elmira_vaccine.models.Nurse;
import com.example.elmira_vaccine.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientRepository {
    private final FirebaseFirestore DB;
    private final String COLLECTION_PATIENT = "patient";
    private final String FIELD_HEALTH_NUMBER = "healthNumber";
    private final String FIELD_NAME = "name";
    private final String FIELD_FIRST_DOSE = "first dose";
    private final String FIELD_SECONDE_DOSE = "second dose";
    private final String FIELD_THIRD_DOSE = "third dose";
    private final String FIELD_NURSE_NAME = "nurse";
    public MutableLiveData<List<Patient>> allPatients = new MutableLiveData<>();

    private final String TAG = this.getClass().getCanonicalName();

    public PatientRepository() {
        this.DB = FirebaseFirestore.getInstance();
    }
    public void getPatient(String healthNumber){
        try{
            DB.collection(COLLECTION_PATIENT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<Patient> patientList = new ArrayList<>();

                    if (task.getResult().isEmpty()){
                        Log.e(TAG, "onSuccess: No data retrieved");
                    }else{
                        Log.e(TAG, "onSuccess: queryDocumentSnapshots" + task.getResult().getDocumentChanges() );
                        DocumentSnapshot data = task.getResult().getDocuments().get(0);
                        for(DocumentSnapshot doc : task.getResult()){

                            Patient currentPatient = new Patient(doc.get("name").toString(), doc.get("healthNumber").toString(), (boolean)doc.get("first dose"), (boolean)doc.get("second dose"), (boolean)doc.get("third dose"),doc.get("nurse").toString(),doc.getId());
                            patientList.add(currentPatient);
                            Log.e(TAG, "onSuccess: currentPatient " + currentPatient.toString());
                        }
                        allPatients.postValue(patientList);
                    }
                }
            });
        }catch (Exception ex){
            Log.e(TAG, "getAllPatients: " + ex.getLocalizedMessage());
        }
    }
    public void addPatient(Patient newPatient){
        try{
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_NAME, newPatient.getName());
            data.put(FIELD_HEALTH_NUMBER, newPatient.getHealthCard());
            data.put(FIELD_FIRST_DOSE, newPatient.isDose1());
            data.put(FIELD_SECONDE_DOSE, newPatient.isDose2());
            data.put(FIELD_THIRD_DOSE, newPatient.isDose3());
            data.put(FIELD_NURSE_NAME, newPatient.getNurseName());

            DB.collection(COLLECTION_PATIENT).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.e(TAG, "onSuccess: Document successfully created wiht id " + documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailier: Error while creating document" + e.getLocalizedMessage());
                }
            });
        }catch (Exception ex){
            Log.e(TAG, "addPatient: " + ex.getLocalizedMessage());
        }

    }
    public void updatePatient(Patient updatedPatient){

        Log.d(TAG, "here " + updatedPatient.toString());
        Map<String, Object> data = new HashMap<>();
        data.put(FIELD_NAME, updatedPatient.getName());
        data.put(FIELD_NURSE_NAME, updatedPatient.getNurseName());
        data.put(FIELD_HEALTH_NUMBER, updatedPatient.getHealthCard());
        data.put(FIELD_FIRST_DOSE, updatedPatient.isDose1());
        data.put(FIELD_SECONDE_DOSE, updatedPatient.isDose2());
        data.put(FIELD_THIRD_DOSE, updatedPatient.isDose3());

        try{
            DB.collection(COLLECTION_PATIENT)
                    .document(updatedPatient.getId())
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e(TAG, "onSuccess: document successfully updated");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to update document" + e.getLocalizedMessage());
                        }
                    });
        }catch (Exception ex){
            Log.e(TAG, "updatePatient: " + ex.getLocalizedMessage() );
        }
    }
}
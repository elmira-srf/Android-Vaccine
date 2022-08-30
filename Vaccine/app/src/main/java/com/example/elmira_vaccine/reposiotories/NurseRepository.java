package com.example.elmira_vaccine.reposiotories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.elmira_vaccine.models.Nurse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.List;

public class NurseRepository {
    private FirebaseFirestore DB;
    private final String COLLECTION_NURSE = "Nurse";
    private final String FIELD_USERNAME = "Username";
    private final String FIELD_PASSWORD = "Password";
    private final String TAG = this.getClass().getCanonicalName();
    public MutableLiveData<List<Nurse>> allNurses = new MutableLiveData<>();


    public String loggedInNurse = "";

    public NurseRepository() {
        this.DB = FirebaseFirestore.getInstance();
    }
    public void getAllNurse(){
        try{
            DB.collection(COLLECTION_NURSE)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Nurse> NurseList = new ArrayList<>();

                            if (queryDocumentSnapshots.isEmpty()){
                                Log.e(TAG, "onSuccess: No data retrieved");
                            }else{
                                Log.e(TAG, "onSuccess: queryDocumentSnapshots" + queryDocumentSnapshots.getDocumentChanges() );

                                for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){
                                    Nurse currentNurse = documentChange.getDocument().toObject(Nurse.class);
                                    currentNurse.setId(documentChange.getDocument().getId());
                                    NurseList.add(currentNurse);
                                    Log.e(TAG, "onSuccess: currentNurse " + currentNurse.toString() );
                                }

                                allNurses.postValue(NurseList);
                                Log.d(TAG,"All Nurses: " + allNurses.toString());
                            }
                        }
                    });
        }catch (Exception ex){
            Log.e(TAG, "getAllNurses: " + ex.getLocalizedMessage());
        }
    }
}

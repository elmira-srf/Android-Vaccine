package com.example.elmira_vaccine.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.elmira_vaccine.MainActivity;
import com.example.elmira_vaccine.R;
import com.example.elmira_vaccine.databinding.ActivityMainBinding;
import com.example.elmira_vaccine.databinding.ActivitySearchAndUpdateBinding;
import com.example.elmira_vaccine.models.Nurse;
import com.example.elmira_vaccine.models.Patient;
import com.example.elmira_vaccine.viewmodels.NurseViewModel;
import com.example.elmira_vaccine.viewmodels.PatientViewModel;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SearchAndUpdate extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getCanonicalName();
    private ActivitySearchAndUpdateBinding binding;
    private Patient newPatient;
    private PatientViewModel patientViewModel;
    private NurseViewModel nurseViewModel;
    private ArrayList<Patient> patientArray = new ArrayList<>();
    private String loggedInNurse;
    private boolean flag = false;
    private Patient foundPatient;
    private boolean isPatientNew = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivitySearchAndUpdateBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnSearch.setOnClickListener(this);
        this.binding.btnSave.setOnClickListener(this);

        Intent intent = getIntent();
        loggedInNurse = intent.getStringExtra("loggedInNurse");


        Log.e(TAG, "logged in nurse " + loggedInNurse);

        this.newPatient = new Patient();
        this.patientViewModel = PatientViewModel.getInstance(this.getApplication());
        this.binding.dose1.setEnabled(false);
        this.binding.dose2.setEnabled(false);
        this.binding.dose3.setEnabled(false);
        this.binding.btnSave.setEnabled(false);
        this.binding.pName.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        if (view != null){
//            boolean isPatientNew = true;
            switch (view.getId()){
                case R.id.btn_save:{
                    Log.d(TAG, "onClick: Save Button Clicked");
                    if(isPatientNew) {
                        Log.d(TAG,"is patient new?(if) " + isPatientNew);
                        this.newPatient.setHealthCard(this.binding.HealthCardNumber.getText().toString());
                        this.newPatient.setName(this.binding.pName.getText().toString());
                        this.newPatient.setDose1(this.binding.dose1.isChecked());
                        this.newPatient.setDose2(this.binding.dose2.isChecked());
                        this.newPatient.setDose3(this.binding.dose3.isChecked());
                        this.newPatient.setNurseName(loggedInNurse);

                        this.patientViewModel.addPatient(this.newPatient);

                    }else{
                        Log.d(TAG,"is patient new? " + isPatientNew);
                        this.newPatient.setName(this.foundPatient.getName());
                        this.newPatient.setHealthCard(this.foundPatient.getHealthCard());
                        this.newPatient.setNurseName(this.loggedInNurse);
                        this.newPatient.setId(this.foundPatient.getId());
                        this.newPatient.setDose1(this.binding.dose1.isChecked());
                        this.newPatient.setDose2(this.binding.dose2.isChecked());
                        this.newPatient.setDose3(this.binding.dose3.isChecked());

                        this.patientViewModel.updatePatient(newPatient);
                    }

                    // resetting the fields
                    this.binding.HealthCardNumber.setText("");
                    this.binding.pName.setText("");
                    this.binding.dose1.setEnabled(false);
                    this.binding.dose2.setEnabled(false);
                    this.binding.dose3.setEnabled(false);
                    this.binding.dose3.setChecked(false);
                    this.binding.dose2.setChecked(false);
                    this.binding.dose1.setChecked(false);

                    Toast.makeText(this, "Patient was add/updated", Toast.LENGTH_SHORT).show();

                    break;
                }
                case R.id.btn_search:{
                    String hn = "";
                    Log.d(TAG, "onClick: Search Button Clicked");
                    if (this.binding.HealthCardNumber.getText().toString().isEmpty()){
                        this.binding.HealthCardNumber.setError("Enter a Health Number");
//                        Log.e(TAG, "-----curr1 " + this.binding.HealthCardNumber.getText().toString());
                    }else{
                        if(this.binding.HealthCardNumber.getText().toString().length() == 5){
                            String currentHealthNumber = this.binding.HealthCardNumber.getText().toString();

//                            Log.e(TAG, "-----curr2 " + currentHealthNumber);
                            this.binding.btnSave.setEnabled(true);
                            //search between patient
                            this.patientViewModel.getPatient(currentHealthNumber);
                            this.patientViewModel.allPatients.observe(this, new Observer<List<Patient>>() {
                                @Override
                                public void onChanged(List<Patient> patient) {
                                    if (patient.isEmpty()){
                                        Log.e("TAG", "-----curr5 onChanged: No documents received");
                                    }else{
                                        for (Patient pat:patient){
                                            Log.e("TAG", "-----curr6 onChanged: pat : " + pat.toString() );
                                            patientArray.add(pat);
                                            Log.e(TAG, "------------" + patientArray.size());

                                            // a function to set up the UI and set the values
                                            setUp(currentHealthNumber);
                                        }
                                    }
                                }
                            });;
                            Log.e(TAG, "-----curr3 " + patientArray.toString());

                        }else{
                            Log.d(TAG, "Health number is invalid");
                            this.binding.HealthCardNumber.setText("");
                            Toast.makeText(this, "Enter 5 digit number", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btr_logout:{
                Intent insertIntent = new Intent(this, MainActivity.class);
                startActivity(insertIntent);
                Toast.makeText(this, "You are logged out", Toast.LENGTH_SHORT).show();

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUp(String currentHealthNumber){
        for(int i=0; i<patientArray.size(); i++){
            Log.e(TAG, "-----curr4 " + currentHealthNumber + " array " + patientArray.get(i).toString());

            if(currentHealthNumber.equals(patientArray.get(i).getHealthCard())){

                isPatientNew = false;
                foundPatient = patientArray.get(i);

                // set the patient info
                this.binding.pName.setText(patientArray.get(i).getName());//set patient name
                this.binding.pName.setEnabled(false);
                //set the doses
                Log.e(TAG, "isDose1"+ patientArray.get(i).isDose1());
                Log.e(TAG, "isDose2"+ patientArray.get(i).isDose2());
                Log.e(TAG, "isDose3"+ patientArray.get(i).isDose3());

                this.binding.dose1.setChecked(patientArray.get(i).isDose1());
                this.binding.dose2.setChecked(patientArray.get(i).isDose2());
                this.binding.dose3.setChecked(patientArray.get(i).isDose3());


                if(patientArray.get(i).isDose1()){
                    this.binding.dose1.setEnabled(false);
                    this.binding.dose2.setEnabled(true);
                }
                if(patientArray.get(i).isDose2()){
                    this.binding.dose2.setChecked(true);
                    this.binding.dose2.setEnabled(false);
                    this.binding.dose3.setEnabled(true);
                }
                if(patientArray.get(i).isDose3()){
                    this.binding.dose3.setChecked(true);
                    this.binding.dose2.setEnabled(false);
                    this.binding.dose3.setEnabled(false);
                }
                if(!patientArray.get(i).isDose3() && !patientArray.get(i).isDose2() && !patientArray.get(i).isDose1()){
                    this.binding.dose1.setEnabled(true);
                    this.binding.dose2.setEnabled(false);
                    this.binding.dose3.setEnabled(false);
                }
                break;
            }else{

                isPatientNew = true;

                if (i+1 == patientArray.size()){
                    this.binding.pName.setEnabled(true);
                    this.binding.pName.setText("");
                    this.binding.dose1.setEnabled(true);
                    this.binding.dose1.setChecked(false);
                    this.binding.dose2.setEnabled(false);
                    this.binding.dose2.setChecked(false);
                    this.binding.dose3.setEnabled(false);
                    this.binding.dose3.setChecked(false);
//                    Toast.makeText(this, "No patient found", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
}
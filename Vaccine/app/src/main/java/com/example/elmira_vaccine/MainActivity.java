package com.example.elmira_vaccine;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.elmira_vaccine.models.Nurse;
import com.example.elmira_vaccine.models.Patient;
import com.example.elmira_vaccine.reposiotories.NurseRepository;
import com.example.elmira_vaccine.reposiotories.PatientRepository;
import com.example.elmira_vaccine.viewmodels.NurseViewModel;
import com.example.elmira_vaccine.viewmodels.PatientViewModel;
import com.example.elmira_vaccine.views.SearchAndUpdate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.elmira_vaccine.R;
import com.example.elmira_vaccine.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
//import com.example.elmira_vaccine.repositories.FriendRepository;
//import com.example.elmira_vaccine.viewmodels.FriendViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    private ActivityMainBinding binding;
    private Patient newPatient;
    private PatientViewModel patientViewModel;
    private NurseRepository nurseRepository;
    private NurseViewModel nurseViewModel;
    private ArrayList<Nurse> nurseArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnSignIn.setOnClickListener(this);

        this.nurseViewModel = NurseViewModel.getInstance(this.getApplication());
        this.nurseRepository = this.nurseViewModel.getRepository();

    }

    @Override
    public void onClick(View view) {
        if (view != null){
            if(view.getId() == R.id.btn_sign_in) {
                Log.d(TAG, "onClick: Sign In Button Clicked");

                nurseRepository.getAllNurse();
                this.validateData();
            }
        }else{
            Log.d(TAG, "No user name found!");
        }
    }
    private void validateData(){
        Boolean validData = true;
        String username = "";
        String password = "";

        if (this.binding.editUserName.getText().toString().isEmpty()){
            this.binding.editPassword.setError("Email Cannot be Empty");
            validData = false;
        }else{
            username = this.binding.editUserName.getText().toString();
        }

        if (this.binding.editPassword.getText().toString().isEmpty()){
            this.binding.editPassword.setError("Password Cannot be Empty");
            validData = false;
        }else {
            password = this.binding.editPassword.getText().toString();
        }

        if (validData){
            this.signIn(username, password);
        }else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }

    }
    private void signIn(String username, String password){
        this.nurseViewModel.getAllNurse();
        this.nurseViewModel.allNurses.observe(this, new Observer<List<Nurse>>() {
            @Override
            public void onChanged(List<Nurse> nurses) {
                if (nurses.isEmpty()){
                    Log.e("TAG", "onChanged: No documents received");
                }else{
                    for (Nurse nrs:nurses){
                        Log.e("TAG", "onChanged: nrs : " + nrs.toString() );
                    }
                    nurseArray.clear();
                    nurseArray.addAll(nurses);
                }
            }
        });

        for(int i=0; i<nurseArray.size(); i++){
            if(nurseArray.get(i).getUserName().equals(username) && nurseArray.get(i).getPassword().equals(password)){
                nurseRepository.loggedInNurse = username;
                this.finishAffinity();
                Intent mainIntent = new Intent(this, SearchAndUpdate.class);
                mainIntent.putExtra("loggedInNurse",username);
                startActivity(mainIntent);
                Toast.makeText(this, "You are logged in.", Toast.LENGTH_SHORT).show();

                break;
            }else{

                Toast.makeText(this, "Your username/password is not valid", Toast.LENGTH_SHORT).show();
                this.binding.editUserName.setText("");
                this.binding.editPassword.setText("");
            }
        }


    }
}
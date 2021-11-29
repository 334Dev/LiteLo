package com.dev334.litelo.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.dev334.litelo.Database.TinyDB;
import com.dev334.litelo.R;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private loginHomeFragment loginHome;
    private FragmentManager fragmentManager;
    private signUpFragment SignupFrag;
    private loginFragment loginFrag;
    private emailVerifyFragment verificationFragment;
    private phoneAuthFragment phoneFragment;
    private OTPVerifyFragment otpFragment;
    private createProfileFragment CreateProfileFragment;
    private String PhoneNo,Username,Organisation,Facebook, Instagram;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ArrayList<String> Organisations,userInterest;
    private TinyDB tinyDB;
    private TextView loginTxt;
    private int FRAGMENT=0; //0>default 1->emailVerification

    private String email, password,  phoneNo, verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginHome=new loginHomeFragment();
        SignupFrag=new signUpFragment();
        loginFrag=new loginFragment();
        verificationFragment=new emailVerifyFragment();
        phoneFragment=new phoneAuthFragment();
        otpFragment=new OTPVerifyFragment();
        CreateProfileFragment=new createProfileFragment();

        tinyDB=new TinyDB(getApplicationContext());

        fragmentManager=getSupportFragmentManager();

        FRAGMENT=getIntent().getIntExtra("FRAGMENT",0);

        if(FRAGMENT==0){
            replaceFragment(loginHome);
        }else if(FRAGMENT==2){
            replaceFragment(CreateProfileFragment);
        }else if(FRAGMENT==1){
            replaceFragment(loginFrag);
        }else{
            replaceFragment(loginFrag);
        }
    }

    public void openLogin(){
        replaceFragment(loginFrag);
    }

    public void openSignup(){
        replaceFragment(SignupFrag);
    }

    public void openVerifyEmail(){
        replaceFragment(verificationFragment);
    }

    public void openPhoneAuth(){
        replaceFragment(phoneFragment);
    }

    public void openPhoneOTP(){
        replaceFragment(otpFragment);
    }

    public void setPhoneNo(String phone, String verifyID, PhoneAuthProvider.ForceResendingToken rToken){
        phoneNo=phone;
        verificationID=verifyID;
        mResendToken=rToken;
    }

    public void openCreateProfile(){
        replaceFragment(CreateProfileFragment);
    }

    public String getPhoneNo(){
        return phoneNo;
    }

    public String getVerificationID(){
        return verificationID;
    }

    public void setSignUpCredentials(String email, String password){
        this.email=email;
        this.password=password;
    }

    public String getSignUpEmail(){
        return email;
    }

    public String getSignUpPassword(){
        return password;
    }

    private void replaceFragment(Fragment fragmentToShow) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // Hide all of the fragments
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            transaction.hide(fragment);
        }

        if (fragmentToShow.isAdded()) {
            // When fragment was previously added - show it
            transaction.show(fragmentToShow);
        } else {
            // When fragment is adding first time - add it
            transaction.add(R.id.LoginContainer, fragmentToShow);
        }

        transaction.commit();
    }

    public void createProfile() {
        //not required currently
    }
}
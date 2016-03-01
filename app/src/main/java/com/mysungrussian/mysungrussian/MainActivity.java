package com.mysungrussian.mysungrussian;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity
        implements TranscribeFragment.OnFragmentInteractionListener, SavedFragment.OnFragmentInteractionListener {
    TranscribeFragment transFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transFrag = new TranscribeFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, transFrag).commit();

    }


    public void tabBtnOnClick (View v){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_transcribe:
                fragmentTransaction.replace(R.id.fragment_container, transFrag);
                fragmentTransaction.commit();
                break;
            case R.id.btn_saved:
                SavedFragment savedFrag = new SavedFragment();
                fragmentTransaction.replace(R.id.fragment_container, savedFrag);
                fragmentTransaction.commit();
                break;
        }
    }

    public void onClickTranscribe (View v){
        hideSoftKeyboard();
        
    }
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow( getCurrentFocus().getWindowToken(), 0);
    }


    public void onFragmentInteraction(Uri uri){

    }
}

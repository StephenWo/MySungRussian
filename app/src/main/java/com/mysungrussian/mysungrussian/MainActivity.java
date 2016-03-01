package com.mysungrussian.mysungrussian;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements TranscribeFragment.OnFragmentInteractionListener, SavedFragment.OnFragmentInteractionListener {
    TranscribeFragment transFrag;
    SavedFragment savedFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transFrag = new TranscribeFragment();
        savedFrag = new SavedFragment();


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
                fragmentTransaction.replace(R.id.fragment_container, savedFrag);
                fragmentTransaction.commit();
                break;
            case R.id.btn_learn:
                //Just put the same empty fragment here
                fragmentTransaction.replace(R.id.fragment_container, savedFrag);
                fragmentTransaction.commit();
                break;
        }
    }

    public void onClickTranscribe (View v){
        //hideSoftKeyboard();
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        EditText input_field = (EditText) findViewById(R.id.input_field);
        String input_words = input_field.getText().toString();
        input_field.clearFocus();

        Log.d("Che", "transcribing, the input is: " +input_words);

        if (!input_words.isEmpty()) {
            String output_ipas = RuleEngine.Transcribe(input_words);
            Log.d("Che", "transcribing, the output is: " + output_ipas);

            EditText output_field = (EditText) findViewById(R.id.output_field);
            output_field.clearFocus();
            output_field.setText(output_ipas);
        }

    }
    /*public void onClickInputField (View v) {
        //hideSoftKeyboard();
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        EditText input_field = (EditText) findViewById(R.id.input_field);
        input_field.clearFocus();
        input_field.hasFocus();
    }*/

    private void hideSoftKeyboard() {
        if (getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    public void onFragmentInteraction(Uri uri){

    }
}

package com.mysungrussian.mysungrussian;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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


        // For hiding keyboard when outside text box is clicked
        setupUI( findViewById(R.id.fragment_container) );
    }

    // Method to hide keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    // Method
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    if(getCurrentFocus() != null) {
                        hideSoftKeyboard(MainActivity.this);
                        getCurrentFocus().clearFocus();
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
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
        input_field.setText("Здравствуйте тотчас же время работы по созданию интеллектуальных систем видеонаблюдения на избирательных линий розлива растительного происхождения товаров для личного использования");
        String input_words = input_field.getText().toString();
        input_field.clearFocus();

        Log.d("Che", "transcribing, the input is: " +input_words);

        if (!input_words.isEmpty()) {
            // Feed into RuleEngine, the result is a string of IPA
            String output_ipas = RuleEngine.Transcribe(input_words);
            Log.d("Che", "transcribing, the output is: " + output_ipas);

            // Tokenize the input and output string, pad spaces to make them equal length

            // Put output
            EditText output_field = (EditText) findViewById(R.id.output_field2);
            output_field.setVisibility(View.VISIBLE);
            output_field.clearFocus();
            output_field.setText(output_ipas);

            // Bring the input_field to front (for now, only allow editting of this)
            input_field.bringToFront();
            input_field.clearFocus();
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

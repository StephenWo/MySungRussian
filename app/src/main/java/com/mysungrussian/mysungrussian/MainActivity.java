package com.mysungrussian.mysungrussian;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
        implements TranscribeFragment.OnFragmentInteractionListener,
        SavedFragment.OnFragmentInteractionListener, LearnFragment.OnFragmentInteractionListener {
    TranscribeFragment transFrag;
    SavedFragment savedFrag;
    LearnFragment learnFrag;


    private String[] tabs = { "Top Rated", "Games", "Movies" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        transFrag = new TranscribeFragment();
        savedFrag = new SavedFragment();
        learnFrag = new LearnFragment();

        // Use the transFrag as the default main fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, transFrag).commit();

        // For hiding keyboard when outside text box is clicked
        setupUI(findViewById(R.id.fragment_container));

        // For tabs
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //android.support.v7.app.ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        // Clicking the toolbar will take user back to transFrag
        myToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Toolbar title clicked", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, transFrag).commit();
            }
        });

    }

    // Method to hide keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    // Method for hiding soft keyboard
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Log.d("Che", "in action_settings");
                return true;

            case R.id.action_learn:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Log.d("Che", "in action_learn, change fragment");
                fragmentTransaction.replace(R.id.fragment_container, learnFrag);
                fragmentTransaction.commit();
                return true;

            case R.id.action_files:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Log.d("Che", "in action_files, change fragment");
                fragmentTransaction.replace(R.id.fragment_container, savedFrag);
                fragmentTransaction.commit();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_items, menu);
        return true;
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
        EditText output_field = (EditText) findViewById(R.id.output_field2);

        //input_field.setText("Здравствуйте тотчас же время работы по созданию интеллектуальных систем видеонаблюдения на избирательных линий розлива растительного происхождения товаров для личного использования");
        String input = input_field.getText().toString();
        input_field.setVisibility(View.INVISIBLE);
        input_field.clearFocus();
        int field_width = input_field.getWidth();
        Log.d("Che", "Field width is "+field_width);

        Log.d("Che", "transcribing, the input is: " + input);

        if (!input.isEmpty()) {
            // Get our output_field~
            output_field.setVisibility(View.INVISIBLE);
            output_field.setFocusable(false);

            // The formatted strings from this trunk of code!
            String formatted_input = "";
            String formatted_output = "";
            int line_width = 0;


            //Split into sentences before feeding into RuleEngine.Transcribe() TODO: put this into RuleEngine
            String[] input_sentences = input.split("[,.!?]");
            for (int counter = 0; counter<input_sentences.length; counter ++) {
                // Feed into RuleEngine, call Transcribe()
                // (the result is a string of IPAs)
                String input_sentence = input_sentences[counter];
                input_sentence = input_sentence.replaceAll("\n", "");   //Strip newlines
                input_sentence = input_sentence.replaceAll(" +", " ");   //Strip spaces if >1 space
                Log.d("Che", "transcribing sentence "+input_sentence);

                String output_ipas = RuleEngine.Transcribe(input_sentence);
                Log.d("Che", "transcribed sentence #" + counter + ", the ipa is:" + output_ipas);

                // Tokenize the input and output string with space token
                String[] input_words = input_sentence.split(" ");
                String[] output_words = output_ipas.split(" ");


                for (int i = 0; i<input_words.length; i++) {
                    String temp_in = input_words[i];
                    String temp_out = output_words[i];
                    Log.d("Che", "input word: "+ temp_in+", output ipa: " + temp_out);

                    // Put strings into EditText for measuring
                    input_field.setText(temp_in);
                    output_field.setText(temp_out);

                    input_field.measure(0, 0);
                    int width = input_field.getMeasuredWidth(); //BUG! I often get wrong width!
                    output_field.measure(0, 0);
                    int width2 = output_field.getMeasuredWidth();
                    Log.d("Che", "input +width: "+ width+", output +width2: " + width2);


                    // Handle breaking into new line
                    /*line_width += (width2>width) ? width2:width;
                    Log.d("Che", "line_width = "+line_width);
                    if (line_width>field_width){
                        Log.d("Che", "oh " + formatted_input);
                        temp_in = "\n" + temp_in;
                        temp_out = "\n" + temp_out;
                        line_width = 0;
                    }*/

                    // TODO: Handle end of sentence, add back the sign
                    if (counter==input_words.length-1) {
                        //temp_in += ".";
                        //temp_out += ".";
                    }

                    // Handle different length: pad space
                    while ( (width2-width) > 4 ){
                        // IPA is longer than word
                        temp_in += " ";
                        width += 7;
                    }
                    while ( (width-width2) > 4 ){
                        // word is longer than IPA
                        temp_out += " ";
                        width2 += 7;
                    }

                    // Always add a space at the end
                    temp_in += " ";
                    temp_out += " ";

                    // Always append words onto formatted strings
                    formatted_input += temp_in;
                    formatted_output += temp_out;

                    //formatted_input = formatted_input.substring(0,formatted_input.length()-2) + ". ";
                    //formatted_output = formatted_output.substring(0,formatted_input.length()-2) + ". ";
                }
            }

            // Put formatted input output into EditText
            input_field.setVisibility(View.VISIBLE);
            input_field.setText(formatted_input);
            output_field.setVisibility(View.VISIBLE);
            output_field.setText(formatted_output);

            // Bring the input_field to front (for now, only allow editing of words, not IPAs)
            input_field.bringToFront();

        }
        else {
            output_field.setText("");
        }
    }
    public void onClickClear(View v) {
        TextView in = (TextView) findViewById(R.id.input_field);
        TextView out = (TextView) findViewById(R.id.output_field2);

        in.setText("");
        out.setText("");

        in.setFocusable(true);
    }

    public void onClickRecord(View v) {
        learnFrag.onClickRecord(v);
    }

    public void onClickPlay(View v) {
        learnFrag.onClickPlay(v);
    }


    public void onFragmentInteraction(Uri uri){

    }
}

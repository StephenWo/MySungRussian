package com.mysungrussian.mysungrussian;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity
        implements TranscribeFragment.OnFragmentInteractionListener, LearnFragment.OnFragmentInteractionListener {
    TranscribeFragment transFrag;
    SavedFragment savedFrag;
    String filename = "";
    LearnFragment learnFrag;

    String current_formatted_input = "";
    String current_formatted_output = "";

    
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

        myColorTint();

    }

    private void myColorTint() {
        int tint = Color.parseColor("#b1b1b1"); //grey
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        // add your drawable resources you wish to tint to the drawables array...
        int drawables[] = { R.drawable.ic_clear_black_24dp};
        for (int id : drawables) {
            Drawable icon = getResources().getDrawable(id);
            icon.setColorFilter(tint,mode);
        }
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
        if(getCurrentFocus() != null) {
            hideSoftKeyboard(MainActivity.this);
            getCurrentFocus().clearFocus();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {

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
                fragmentTransaction.addToBackStack("savedFrag_backTag");
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


    // Buttons in TranscribeFragment
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
        Log.d("Che", "Field width is " + field_width);

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

                if (input_sentence.isEmpty()){
                    continue;
                }

                input_sentence = input_sentence.replaceAll("\n", "");   //Strip newlines
                input_sentence = input_sentence.replaceAll(" +", " ");   //Strip spaces if >1 space
                Log.d("Che", "transcribing sentence "+input_sentence);

                String output_ipas = RuleEngine.Transcribe(input_sentence);
                output_ipas = output_ipas.substring(1);
                Log.d("Che", "transcribed sentence #" + counter + ", the ipa is:" + output_ipas);

                // Tokenize the input and output string with space token
                String[] input_words = input_sentence.split(" ");
                String[] output_words = output_ipas.split(" ");


                // Put word-pairs in one by one?
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
                }
            }

            // Put formatted input output into EditText
            /*input_field.setVisibility(View.VISIBLE);
            input_field.setText(formatted_input);
            output_field.setVisibility(View.VISIBLE);
            output_field.setText(formatted_output);*/

            current_formatted_input = formatted_input;
            current_formatted_output = formatted_output;
            mySetText();

        }
        else {
            output_field.setText("");
        }
    }

    public void mySetText (){
        Log.d("Che", "mySetText");
        if (!current_formatted_input.isEmpty()){
            EditText input_field = (EditText) findViewById(R.id.input_field);
            EditText output_field = (EditText) findViewById(R.id.output_field2);

            // Put formatted input output into EditText
            input_field.setVisibility(View.VISIBLE);
            input_field.setText(current_formatted_input);
            output_field.setVisibility(View.VISIBLE);
            output_field.setText(current_formatted_output);

            // Bring the input_field to front (for now, only allow editing of words, not IPAs)
            input_field.bringToFront();

        }

    }

    public void onClickClear(View v) {
        Log.d("Che", "onClickClear");

        current_formatted_input = "";
        current_formatted_output = "";

        EditText input_field = (EditText) findViewById(R.id.input_field);
        EditText output_field = (EditText) findViewById(R.id.output_field2);

        // Put formatted input output into EditText
        input_field.setVisibility(View.VISIBLE);
        input_field.setText(current_formatted_input);
        output_field.setVisibility(View.VISIBLE);
        output_field.setText(current_formatted_output);

        input_field.bringToFront();

    }

    public void onClickSave (View v){
        showInputDialog();
    }

    // Buttons in LearnFragment
    public void onClickRecord(View v) {
        learnFrag.onClickRecord(v);
    }

    public void onClickPlay(View v) {
        learnFrag.onClickPlay(v);
    }


    public void onFragmentInteraction(Uri uri){

    }

    protected void showInputDialog(){
        EditText inputFile = (EditText)findViewById(R.id.input_field);
        final String input_Field = inputFile.getText().toString();

        EditText outputFile = (EditText)findViewById(R.id.output_field2);
        final String output_Field = outputFile.getText().toString();

        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.save_trans_pop, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);
        final EditText file_name = (EditText) promptView.findViewById(R.id.enter_file_name);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        filename = file_name.getText().toString()+ ".txt";
                        Log.d("myTag", file_name.getText().toString());
                        writeFile(filename, input_Field, output_Field);
                        filename = "";
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

    public void writeFile(String filename, String input_Field, String output_Field){
        try {
            Log.d("myTag", "writefile is called");
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(output_Field);

            Log.d("myTag", output_Field + " is saved into " + filename);
            outputStreamWriter.write(input_Field);
            Log.d("myTag", input_Field + " is saved into " + filename);
            outputStreamWriter.close();
            fos.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }


}

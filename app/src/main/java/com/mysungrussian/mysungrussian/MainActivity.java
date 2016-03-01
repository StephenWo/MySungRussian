package com.mysungrussian.mysungrussian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIPA();
    }

    private String getIPA(){
        String word = "Здравствуйте, мир!";
        String ipa = new String();
        RuleEngine rule = new RuleEngine();
        ipa = rule.Transcribe(word);
        return ipa;
    }
}


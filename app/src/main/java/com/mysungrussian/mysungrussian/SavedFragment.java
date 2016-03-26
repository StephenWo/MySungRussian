package com.mysungrussian.mysungrussian;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class SavedFragment extends Fragment {

    public static String file_content = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result  = inflater.inflate(R.layout.fragment_saved, container, false);


        String[] file_names;
        file_names = getActivity().fileList();

        ListView listView = (ListView)result.findViewById(R.id.listView_saved);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, file_names);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = ((TextView)view).getText().toString();
                Log.d("myTag", "here is my tag" + tag);
                String dir = getActivity().getBaseContext().getFilesDir().getAbsolutePath();
                Log.d("myTag", "my path is " + dir);
                try {
                    FileInputStream fin = getActivity().getBaseContext().openFileInput(tag);
                    InputStreamReader inputStreamReader = new InputStreamReader(fin);
                    BufferedReader br = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = br.readLine()) != null) {
                        file_content +=line;
                        Log.d("myTag", "this line is read" + line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                loadFile_frag loadFileFrag  = new loadFile_frag();
                fragmentTransaction.replace(R.id.fragment_container, loadFileFrag);
                fragmentTransaction.commit();
                file_content = "";
            }
        });

        return result;
    }

}

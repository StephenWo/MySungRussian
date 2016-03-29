package com.mysungrussian.mysungrussian;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class SavedFragment extends Fragment {

    public static String file_ipa = "";
    public static String file_word = "";

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                           int position, long arg3) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Do you want to delete this file ? ");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String tag = ((TextView) view).getText().toString();
                                String dir = getActivity().getBaseContext().getFilesDir().getAbsolutePath();
                                File file = new File (dir, tag);
                                boolean deleted = file.delete();
                                Toast.makeText(getActivity().getBaseContext(), "File is deleted",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                file_ipa = "";
                file_word = "";
                String tag = ((TextView) view).getText().toString();
                String dir = getActivity().getBaseContext().getFilesDir().getAbsolutePath();
                try {
                    FileInputStream fin = getActivity().getBaseContext().openFileInput(tag);
                    InputStreamReader inputStreamReader = new InputStreamReader(fin);
                    BufferedReader br = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = br.readLine()) != null) {
                        file_ipa += line;
                        if ((line = br.readLine()) != null) {
                            file_word += line;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                MainActivity ma = (MainActivity) getActivity();
                ma.current_formatted_output = file_ipa;
                ma.current_formatted_input = file_word;

                fragmentTransaction.replace(R.id.fragment_container, ma.transFrag);
                fragmentTransaction.commit();

            }
        });

        return result;
    }

    public void onBackPressed() {
        FragmentManager fm = getActivity().getFragmentManager();
        fm.popBackStack();
    }


}

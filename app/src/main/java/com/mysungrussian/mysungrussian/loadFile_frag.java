package com.mysungrussian.mysungrussian;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class loadFile_frag extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_file_frag, container, false);
        TextView output_field = (TextView)view.findViewById(R.id.open_file);
        output_field.setText(SavedFragment.file_content);
        return view;
    }


}

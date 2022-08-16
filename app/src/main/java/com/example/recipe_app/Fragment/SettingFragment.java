package com.example.recipe_app.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.recipe_app.R;


public class SettingFragment extends Fragment {

    RelativeLayout updte_pass, updt_email;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        updte_pass = view.findViewById(R.id.update_pass);
        updt_email = view.findViewById(R.id.email);

        updte_pass.setOnClickListener(view1 ->  {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new UpdatePassword());
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);

        });

        updt_email.setOnClickListener(view1 ->  {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new UpdateEmailFragment());
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);


        });

        return view;
    }
}
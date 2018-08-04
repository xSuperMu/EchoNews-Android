package com.muhammadelsayed.echo.Fragments.Shortcut;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhammadelsayed.echo.R;
import com.muhammadelsayed.echo.database.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Saved#shortcutsFragment2Instance} factory method to
 * create an instance of this fragment.
 */
public class Saved extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Saved";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Saved() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static Saved shortcutsFragment2Instance() {
        Saved fragment = new Saved();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DatabaseHelper db = new DatabaseHelper(getActivity());
        db.getAllArticles();
        Log.wtf(TAG, "onCreateView: SAVED ARTICLES = " + db.getAllArticles());
        
        return inflater.inflate(R.layout.saved_shortcuts_tab, container, false);
    }

}

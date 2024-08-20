package gr.aueb.cf.agronitor.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.aueb.cf.agronitor.R;

/**
 * A Fragment that displays a message if the specific greenhouse has no measurements.
 */
public class StatsFragmentEmpty extends Fragment {

    public StatsFragmentEmpty() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats_empty, container, false);
    }
}
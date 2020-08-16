package com.example.a1l1_helloworld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CitiesFragment extends Fragment implements IRVOnItemClick {

    private RecyclerView recyclerView;
    private RecyclerDataAdapter adapter;
    private ArrayList<String> listData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        listData = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Cities)));
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (Objects.requireNonNull(getActivity()).getBaseContext());
        adapter = new RecyclerDataAdapter(listData, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String itemText) {
        EventBus.getBus().post(new SomeEvent(itemText));
    }
}
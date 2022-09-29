package com.example.startimageclassify.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.startimageclassify.R;
import com.example.startimageclassify.classes.InfoTableAdapter;
import com.example.startimageclassify.classes.Spacing;

import java.util.ArrayList;
import java.util.Set;

public class fragment_info extends Fragment {

    ArrayList<Spacing> spacingList = new ArrayList<>();
    View view;
    String value;

    RecyclerView info_recycler;
    InfoTableAdapter InfoAdapter;

    public interface UsersProvider {
//        ArrayList<Spacing> getSpacing();
        String getVegsName();
    }

    private UsersProvider usersProvider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        usersProvider = (UsersProvider) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_info, container, false);

//        spacingList = usersProvider.getSpacing();
        value = usersProvider.getVegsName();
        SetUpInfoFor();

        info_recycler = view.findViewById(R.id.info_recycler);
        info_recycler.setHasFixedSize(true);
        InfoAdapter = new InfoTableAdapter(this.getContext(), spacingList);

        info_recycler.setAdapter(InfoAdapter);
        info_recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    private void SetUpInfoFor() {

        String[] info_VegeName = null;
        String[] info_VegeSpacing = null;

        switch (value) {

            case "Bitter":
                info_VegeName = getResources().getStringArray(R.array.class_2_test_1);
                info_VegeSpacing = getResources().getStringArray(R.array.class_2_test_1_spacing);
                break;

            case "Cabbage":
                info_VegeName = getResources().getStringArray(R.array.class_1_test_1);
                info_VegeSpacing = getResources().getStringArray(R.array.class_1_test_1_spacing);
                break;

            case "Chili":
                info_VegeName = getResources().getStringArray(R.array.class_1_test_2);
                info_VegeSpacing = getResources().getStringArray(R.array.class_1_test_2_spacing);
                break;

            case "Eggplant":
                info_VegeName = getResources().getStringArray(R.array.class_2_test_2);
                info_VegeSpacing = getResources().getStringArray(R.array.class_2_test_2_spacing);
                break;

            case "Ginger":
                info_VegeName = getResources().getStringArray(R.array.class_1_test_3);
                info_VegeSpacing = getResources().getStringArray(R.array.class_1_test_3_spacing);
                break;

            case "Lettuce":
                info_VegeName = getResources().getStringArray(R.array.class_2_test_3);
                info_VegeSpacing = getResources().getStringArray(R.array.class_2_test_3_spacing);
                break;

            case "Onion":
                info_VegeName = getResources().getStringArray(R.array.class_2_test_4);
                info_VegeSpacing = getResources().getStringArray(R.array.class_2_test_4_spacing);
                break;

            case "Pechay":
                info_VegeName = getResources().getStringArray(R.array.class_1_test_4);
                info_VegeSpacing = getResources().getStringArray(R.array.class_1_test_4_spacing);
                break;

            case "Sweet Potato":
                info_VegeName = getResources().getStringArray(R.array.class_2_test_5);
                info_VegeSpacing = getResources().getStringArray(R.array.class_2_test_5_spacing);
                break;

            case "Tomato":
                info_VegeName = getResources().getStringArray(R.array.class_1_test_5);
                info_VegeSpacing = getResources().getStringArray(R.array.class_1_test_5_spacing);
                break;

            default:
                info_VegeName = getResources().getStringArray(R.array.no_available_data);
                info_VegeSpacing = getResources().getStringArray(R.array.no_available_data_spacing);
                break;    }

            for (int i = 0; i < info_VegeName.length; i++) {
                spacingList.add(new Spacing(info_VegeName[i], info_VegeSpacing[i]));
            }
    }
}
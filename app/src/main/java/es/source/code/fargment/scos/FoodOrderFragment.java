package es.source.code.fargment.scos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import es.source.code.activity.scos.FoodView;
import es.source.code.activity.scos.R;
import es.source.code.adapter.scos.FoodOrderAdapter;

@SuppressLint("ValidFragment")
public class FoodOrderFragment extends Fragment {

    private int location;
    private Activity activity;
    private Context context;

    @SuppressLint("ValidFragment")
    public FoodOrderFragment(int location, Context context) {
        this.location = location;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        RecyclerView rv = rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        FoodOrderAdapter adapter = null;
        FoodView f = new FoodView();
        adapter = new FoodOrderAdapter(location,context);

        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}
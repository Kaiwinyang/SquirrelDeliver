package com.kaiwin.squirreldeliver;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaiwin.squirreldeliver.Dao.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentOrdersInSuspense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOrdersInSuspense extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;
    SimpleAdapter adapter;

    List<HashMap<String, String>> listForOrderInSuspense = new ArrayList<>();

    DatabaseReference orderRef = Order.getmOrdersDataRef();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentOrdersInSuspense() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOrdersInSuspense.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOrdersInSuspense newInstance(String param1, String param2) {
        FragmentOrdersInSuspense fragment = new FragmentOrdersInSuspense();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listForOrderInSuspense.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    HashMap<String, String> o = new HashMap<>();
                    o.put("title", order.startAt);
                    listForOrderInSuspense.add(o);
                    Log.i(Tool.TAG, snapshot.toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("DatabaseError")
                        .setMessage(databaseError.getMessage().toString())
                        .create()
                        .show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_orders_suspense, container, false);

        getActivity().setTitle(R.string.order_in_suspense);
        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //必須這麼寫，否則白屏
        View empty = View.inflate(getContext(), R.layout.nothing_with_a_smile_face, null/*(ViewGroup) listView.getParent()*/);
        ((ViewGroup) listView.getParent()).addView(empty);
        listView.setEmptyView(empty);

        String[] from = {"title"};
        int[] to = {R.id.item_title};

        adapter = new SimpleAdapter(getActivity(), listForOrderInSuspense, R.layout.simple_item, from, to);

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
//            listForOrderInSuspense.remove(position);
//            adapter.notifyDataSetChanged();
            orderRef.child(listForOrderInSuspense.get(position).get(from[0])).removeValue();

            return true;
        });

        Log.v(Tool.TAG, "FragmentOrdersInSuspense:onActivityCreated");
    }
}

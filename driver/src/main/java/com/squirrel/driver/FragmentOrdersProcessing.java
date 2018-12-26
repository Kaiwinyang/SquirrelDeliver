package com.squirrel.driver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squirrel.driver.Dao.Order;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentOrdersProcessing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOrdersProcessing extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;
    OrderAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child("received order");

    class OrderAdapter extends ArrayAdapter<Order> {
        private int mResourceId;

        public OrderAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.mResourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(mResourceId, null);

            TextView title = view.findViewById(R.id.item_title);
            TextView consigneeName = view.findViewById(R.id.textViewConsigneeName);
            TextView consignorName = view.findViewById(R.id.textViewConsignorName);
            TextView consignorPhone = view.findViewById(R.id.textViewConsignorPhone);
            TextView consigneePhone = view.findViewById(R.id.textViewConsigneePhone);
            TextView consignorAddress = view.findViewById(R.id.textViewAddressOfConsignor);
            TextView consigneeAddress = view.findViewById(R.id.textViewAddressOfConsignee);

            Order o = getItem(position);
            title.setText(o.startAt + "(點擊確認送達)");
            consignorName.setText(o.consignor);
            consignorPhone.setText(o.phoneFrom);
            consigneeName.setText(o.consignee);
            consigneePhone.setText(o.phoneTo);
            consigneeAddress.setText(o.addressOfConsignee);
            consignorAddress.setText(o.addressOfConsignor);

            //Button button = view.findViewById(R.id.button);
            //button.setTag(position);
            //button.setOnClickListener(v -> orderRef.child(adapter.getItem(position).startAt).removeValue());

            return view;
        }
    }

    public FragmentOrdersProcessing() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FragmentOrdersInSuspense.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOrdersProcessing newInstance(String param1) {
        FragmentOrdersProcessing fragment = new FragmentOrdersProcessing();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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

        adapter = new OrderAdapter(getActivity(), R.layout.simple_item);

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot snapshotUID : dataSnapshot.getChildren()) {//到UID這一層了

                    //Log.i("FATAL", snapshotUID.toString());
                    Order order = snapshotUID.getValue(Order.class);
                    if (order.courierUid.equals(mAuth.getUid())) {
                        order.setMyDBRef(snapshotUID.getRef());
                        adapter.add(order);
                    }

                }
                //adapter.notifyDataSetChanged();

                Log.i(Tool.TAG, "onDataChange");
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
        View view = inflater.inflate(R.layout.fragment_orders_in_suspense, container, false);

        listView = view.findViewById(R.id.listView);
        ActivityMajor activity = (ActivityMajor) getActivity();
        activity.setTitle(R.string.title_orders_in_suspense);


        //必須這麼寫，否則白屏
        View empty = View.inflate(getContext(), R.layout.nothing_with_a_smile_face, null/*(ViewGroup) listView.getParent()*/);
        ((ViewGroup) listView.getParent()).addView(empty);
        listView.setEmptyView(empty);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View v, int position, long id) -> {
            Order order = adapter.getItem(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setIcon(R.mipmap.ic_launcher).setTitle("確認送達")
                    .setMessage("確認送達 \n" + order.consignee + "\n" + order.addressOfConsignee)
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            order.doFinishAnOrderAndDeleteItsOriginData();
                            Toast.makeText(getContext(), "確認送達", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });
            builder.create().show();
        });

        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        listView.setOnItemLongClickListener((parent, view, position, id) -> {
//            Order.doReceiveAnOrderAndDeleteItsOriginData(adapter.getItem(position).getMyDBRef());
//            return true;
//        });
//
//        Log.v(Tool.TAG, "FragmentOrdersInSuspense:onActivityCreated");
//    }

}

package com.kaiwin.squirreldeliver.Dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order {

    public String uid, consignor, consignee, phoneFrom, phoneTo, selectedOption;


    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Order(String consignor, String consignee, String phoneFrom, String phoneTo, String selectedOption) {
        this.consignor = consignor;
        this.consignee = consignee;
        this.phoneFrom = phoneFrom;
        this.phoneTo = phoneTo;
        this.selectedOption = selectedOption;

        this.uid = user.getUid();
    }


    public Task<Void> createNewOrder() {
        return db.child("orders")
                .child("new order")
                .child(uid)
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(this);
    }


}

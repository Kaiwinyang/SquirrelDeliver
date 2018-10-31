package com.kaiwin.squirreldeliver.Dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order {

    public String uid, consignor, consignee, phoneFrom, phoneTo, selectedOption, startAt, processedAt, deliveredAt;

    public String courierUid, status;

    public String addressOfConsignor, addressOfConsignee;

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static DatabaseReference getOrdersDataRef() {
        return db.child("orders").child("new order").child(mAuth.getCurrentUser().getUid());
    }

    //Firebase requires an non-arg constructor
    public Order() {
    }

    public Order(String consignor, String consignee, String phoneFrom, String phoneTo, String selectedOption,
                 String addressOfConsignor,
                 String addressOfConsignee) {
        this.consignor = consignor;
        this.consignee = consignee;
        this.phoneFrom = phoneFrom;
        this.phoneTo = phoneTo;
        this.selectedOption = selectedOption;
        this.addressOfConsignor = addressOfConsignor;
        this.addressOfConsignee = addressOfConsignee;

        this.uid = mAuth.getUid();
    }


    public Task<Void> createNewOrder() {
        this.startAt = String.valueOf(System.currentTimeMillis());
        return Order.getOrdersDataRef().child(this.startAt).setValue(this);
    }


}

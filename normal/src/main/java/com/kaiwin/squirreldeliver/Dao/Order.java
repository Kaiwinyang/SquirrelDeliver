package com.kaiwin.squirreldeliver.Dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order {

    public String uid, consignor, consignee, phoneFrom, phoneTo, selectedOption, startAt, processedAt, deliveredAt;

    public String courierUid, status;

    public String addressOfConsignor, addressOfConsignee;

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static DatabaseReference mOrdersDataRef = db.child("orders").child("new order").child(user.getUid());

    public static DatabaseReference getmOrdersDataRef() {
        return mOrdersDataRef;
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

        this.uid = user.getUid();
    }


    public Task<Void> createNewOrder() {
        this.startAt = String.valueOf(System.currentTimeMillis());
        return mOrdersDataRef.child(this.startAt).setValue(this);
    }


}

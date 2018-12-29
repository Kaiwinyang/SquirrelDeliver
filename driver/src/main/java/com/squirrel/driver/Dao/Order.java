package com.squirrel.driver.Dao;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squirrel.driver.Tool;

@IgnoreExtraProperties
public class Order {

    public final static String[] STATUS = {"received", "accomplished"};

    public String uid, consignor, consignee, phoneFrom, phoneTo, selectedOption, startAt, processedAt, deliveredAt;

    public String courierUid, courierEmail, status;

    public String addressOfConsignor, addressOfConsignee;

    private DatabaseReference myDBRef;

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static DatabaseReference getMyOrdersDataRef() {
        return db.child("orders").child("new order")/*.child(mAuth.getUid())*/;
    }

    public static DatabaseReference getOrdersDataRefWithoutUserId() {
        return db.child("orders").child("new order");
    }

    public DatabaseReference getMyDBRef() {
        return myDBRef;
    }

    public void setMyDBRef(DatabaseReference myDBRef) {
        this.myDBRef = myDBRef;
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
        return Order.getMyOrdersDataRef().child(this.startAt).setValue(this);
    }

    public static void doReceiveAnOrderAndDeleteItsOriginData(DatabaseReference orderRef) {
        orderRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Order order = mutableData.getValue(Order.class);
                if (order == null) {
                    return Transaction.success(mutableData);
                }

                orderRef.removeValue();

                order.courierEmail = mAuth.getCurrentUser().getEmail();
                order.courierUid = mAuth.getCurrentUser().getUid();
                order.status = STATUS[0];


                // Set value and report transaction success
                //mutableData.setValue(order);
                db.child("orders").child("received order").child(order.startAt).setValue(order);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(Tool.TAG,
                        "doReceiveAnOrderAndDeleteItsOriginData Transaction:onComplete:" + databaseError);
            }
        });
    }

    public void doFinishAnOrderAndDeleteItsOriginData(/*DatabaseReference orderRef*/) {
        DatabaseReference orderRef = this.myDBRef;
        orderRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Order order = mutableData.getValue(Order.class);
                if (order == null) {
                    return Transaction.success(mutableData);
                }

                orderRef.removeValue();

                order.courierUid = mAuth.getCurrentUser().getUid();
                order.courierEmail = mAuth.getCurrentUser().getEmail();
                order.status = STATUS[1];

                // Set value and report transaction success
                //mutableData.setValue(order);
                db.child("orders").child("finished order").child(order.startAt).setValue(order);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(Tool.TAG,
                        "doFinishAnOrderAndDeleteItsOriginData Transaction:onComplete:" + databaseError);
            }
        });
    }

}

package com.example.root.mobilefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Shop {
    String uid;
    String sid;
    String name;
    String address;
    Map<String, Double> loc;
    String open_hour;
    String close_hour;
    Bitmap avatar;
}

public class Backend {
    public interface Callback<T> {
        public void call(T data);
    }

    public static void getShop(final String sid, final Callback<Shop> cb) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("shops").getChildren()) {
                    Shop shop = ds.getValue(Shop.class);
                    if (shop.sid .equals(sid)) {
                        cb.call(shop);
                    }
                }
                cb.call(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static void signUp(String username, String password, final Bitmap chosenAvatar, final Callback<String> cb) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(username + "@gmail.com", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    cb.call(task.getResult().getUser().getUid());
                }
                else {
                    cb.call(null);
                }
            }
        });
    }

    public static void downloadAvatar(final String filename, final Callback<Bitmap> cb) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference(filename);
        ref.getBytes(1 << 20).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("btag", "avatar download succeed " + filename + ", byte length " + bytes.length);
                cb.call(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("btag", "avatar download failed " + filename);
            }
        });
    }

    public static void uploadAvatar(String filename, Bitmap chosenAvatar) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if (chosenAvatar != null) {
            StorageReference ref = storage.getReference();
            final StorageReference avatarRef = ref.child(filename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            chosenAvatar.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] data = baos.toByteArray();
            UploadTask uploadTask = avatarRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("btag", "avatar uploaded succeed: " + avatarRef.getName());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("btag", "avatar upload failed " + avatarRef.getName());
                }
            });
        }
    }

    public static void addShop(Context context, String uid, String shopName, String address, double lat, double lng, String openHour, String closeHour, final Callback<String> cb) {
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        HashMap<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("name", shopName);
        params.put("address", address);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("open_hour", openHour);
        params.put("close_hour", closeHour);

        Log.d("btag", "uid " + params.get("uid"));
        Log.d("btag", "shopname " + params.get("name"));
        Log.d("btag", "address " + params.get("address"));
        Log.d("btag", "lat " + params.get("lat"));
        Log.d("btag", "lng " + params.get("lng"));
        Log.d("btag", "openHour " + params.get("open_hour"));
        Log.d("btag", "closeHour " + params.get("close_hour"));

        Task<String> addShop = functions.getHttpsCallable("addShop")
                .call(params)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HttpsCallableResult result = task.getResult();
                        Log.d("btag", "result " + result);
                        return ((Map<String, String>)result.getData()).get("sid");
                    }
                });
        addShop.addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            Log.d("btag", "res " + task.getResult());
                            cb.call(task.getResult());
                        }
                        else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException fe = (FirebaseFunctionsException) e;
                            }
                        }
                    }
                });
    }

    public static void getMyShops(final Callback<List<Shop>> cb) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = database.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Shop> myShops = new ArrayList<>();
                for (DataSnapshot ref : dataSnapshot.child("shops").getChildren()) {
                    final Shop shop = ref.getValue(Shop.class);
//                    Log.d("btag", "name" + shop.name);
//                    Log.d("btag", "address" + shop.address);
//                    Log.d("btag", "sid " + shop.sid);
//                    Log.d("btag", "uid " + shop.uid);
//                    Log.d("btag", "open hour " + shop.open_hour);
//                    Log.d("btag", "close hour " + shop.close_hour);
//                    Log.d("btag", "lat " + shop.loc.get("lat").toString());
//                    Log.d("btag", "lng " + shop.loc.get("lng").toString());
//                    Log.d("btag", "getUid " + currentUser.getUid());
                    if (shop.uid.equals(currentUser.getUid())) {
                        Log.d("btag", "ecec " + shop.sid);
                        myShops.add(shop);
                    }
                }
                cb.call(myShops);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("btag", "DatabaseError getMyShops");
            }
        });
    }
}

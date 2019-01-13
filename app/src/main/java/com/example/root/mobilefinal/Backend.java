package com.example.root.mobilefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

class Shop {
    String uid;
    String sid;
    String name;
    String address;
    Bitmap avatar;
    double lat;
    double lng;
    Time openHour;
    Time closeHour;
}

public class Backend {
    public interface Callback<T> {
        public void call(T data);
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
}

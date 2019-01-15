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
    Long likes;

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sid", sid);
        map.put("uid", uid);
        map.put("name", name);
        map.put("address", address);
        map.put("loc", loc);
        map.put("open_hour", open_hour);
        map.put("close_hour", close_hour);
//        map.put("likes", likes);
        return map;
    }
}

class Item {
    String name;
    String iid;
    String sid;
    String description;
    String category;
    String price;
    String quantity;
    Map<String, String> variation;
    String buys;

    public Map<String,Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("iid", iid);
        map.put("sid", sid);
        map.put("description", description);
        map.put("category", category);
        map.put("price", price);
        map.put("quantity", quantity);
        map.put("variation", variation);
        map.put("buys", buys);
        return map;
    }
}

public class Backend {
    static HashMap<String, Object> cache;
    static {
        cache = new HashMap<>();
    }

    public interface Callback<T> {
        public void call(T data);
    }

    public static void getShop(final String sid, final Callback<Shop> cb) {
        Log.d("btag", "getShop sid " + sid);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("shops").getChildren()) {
                    Shop shop = ds.getValue(Shop.class);
                    Log.d("btag", "shop iterable: sid " + shop.sid);
                    if (shop.sid .equals(sid)) {
                        cb.call(shop);
                        ref.removeEventListener(this);
                        return;
                    }
                }
                cb.call(null);
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ref.removeEventListener(this);
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
        if (cache.containsKey(filename)) {
            cb.call((Bitmap)cache.get(filename));
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference(filename);
        ref.getBytes(1 << 20).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("btag", "avatar download succeed " + filename + ", byte length " + bytes.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                cache.put(filename, bitmap);
                cb.call(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("btag", "avatar download failed " + filename);
            }
        });
    }

    public static void uploadAvatar(final String filename, Bitmap chosenAvatar) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if (chosenAvatar != null) {
            cache.put(filename, chosenAvatar);
            StorageReference ref = storage.getReference();
            final StorageReference avatarRef = ref.child(filename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            chosenAvatar.compress(Bitmap.CompressFormat.JPEG, 60, baos);

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

    public static void addShop(Context context, String uid, final String shopName, String address, double lat, double lng, String openHour, String closeHour, final Callback<String> cb) {
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
                            Log.d("btag", "addShop failed " + shopName);
                            cb.call(null);
                        }
                    }
                });
    }

    public static void getMyShops(final Callback<List<Shop>> cb) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("btag", "getUid " + currentUser.getUid());

        getAllShops(new Callback<List<Shop>>() {
            @Override
            public void call(List<Shop> data) {
                ArrayList<Shop> myShops = new ArrayList<>();
                for (Shop shop : data) {
                    if (shop.uid.equals(currentUser.getUid())) {
                        myShops.add(shop);
                    }
                }
                cb.call(myShops);
            }
        });
    }

    public static void getAllItems(final Callback<List<Item>> cb) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot ref : dataSnapshot.child("items").getChildren()) {
                    Item item = ref.getValue(Item.class);
                    Log.d("btag", "name " + item.name);
                    Log.d("btag", "iid " + item.iid);
                    Log.d("btag", "sid " + item.sid);
                    Log.d("btag", "description " + item.description);
                    Log.d("btag", "category " + item.category);
                    Log.d("btag", "price " + item.price);
                    Log.d("btag", "quantity " + item.quantity);
//                    Log.d("btag", "buys " + item.buys);
                    Log.d("btag", "color" + item.variation.get("color"));
                    Log.d("btag", "size" + item.variation.get("size"));

                    items.add(item);
                }
                cb.call(items);
                databaseReference.removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("btag", "DatabaseError getMyShops");
                databaseReference.removeEventListener(this);
            }
        });
    }

    public static void getAllShops(final Callback<List<Shop>> cb) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Shop> shops = new ArrayList<>();
                for (DataSnapshot ref : dataSnapshot.child("shops").getChildren()) {
                    final Shop shop = ref.getValue(Shop.class);
                    Log.d("btag", "name" + shop.name);
                    Log.d("btag", "address" + shop.address);
                    Log.d("btag", "sid " + shop.sid);
                    Log.d("btag", "uid " + shop.uid);
                    Log.d("btag", "open hour " + shop.open_hour);
                    Log.d("btag", "close hour " + shop.close_hour);
                    Log.d("btag", "lat " + shop.loc.get("lat").toString());
                    Log.d("btag", "lng " + shop.loc.get("lng").toString());
                    shops.add(shop);
                }
                databaseReference.removeEventListener(this);
                cb.call(shops);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("btag", "DatabaseError getMyShops");
                databaseReference.removeEventListener(this);
            }
        });
    }

    public static void addItem(final String sid, final String name, String description, String category, String price, String quantity, String color, String size, final Callback<String> cb) {
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        Map<String, String> params = new HashMap<>();
        params.put("sid", sid);
        params.put("name", name);
        params.put("description", description);
        params.put("category", category);
        params.put("price", price);
        params.put("quantity", quantity);
        params.put("color", color);
        params.put("size", size);

        functions
                .getHttpsCallable("addItem")
                .call(params).continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                return ((Map<String, String>)task.getResult().getData()).get("iid");
            }
        }).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    cb.call(task.getResult());
                }
                else {
                    Log.d("btag", "add item failed " + name + " " + sid);
                    cb.call(null);
                }
            }
        });
    }

    public static void updateItem(final String iid, final String name, final String description, final String category, final String price, final String quantity, final String color, final String size, final Callback<Boolean> cb) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.child("items").getChildren()) {
                    Item item = ds.getValue(Item.class);
                    if (item.iid.equals(iid)) {
                        item.name = name;
                        item.description = description;
                        item.category = category;
                        item.price = price;
                        item.quantity = quantity;
                        item.variation.put("color", color);
                        item.variation.put("size", size);
                        ds.getRef().updateChildren(item.toMap());
                        cb.call(true);
                        ref.removeEventListener(this);
                        return;
                    }
                }
                cb.call(false);
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ref.removeEventListener(this);
            }
        });
    }

    public static void getShopItems(final String sid, final Callback<List<Item>> cb) {
        Log.d("btag", "getShopItems " + sid);
        getAllItems(new Callback<List<Item>>() {
            @Override
            public void call(List<Item> data) {
                ArrayList<Item> shopItems = new ArrayList<>();
                for (Item item : data) {
                    if (item.sid.equals(sid)) {
                        shopItems.add(item);
                        Log.d("btag", "shop item " + sid + " " + item.iid);
                    }
                }
                cb.call(shopItems);
            }
        });
    }

    public static void updateShop(final String sid, final String name, final String address, final String openHour, final String closeHour, final Callback<Boolean> cb) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("shops").getChildren()) {
                    Shop shop = ds.getValue(Shop.class);
                    if (shop.sid.equals(sid)) {
                        shop.name = name;
                        shop.address = address;
                        shop.open_hour = openHour;
                        shop.close_hour = closeHour;
                        ds.getRef().setValue(shop.toMap());
                        cb.call(true);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void deleteShop(final String sid, final Callback<Boolean> cb) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("shops").getChildren()) {
                    Shop shop = ds.getValue(Shop.class);
                    if (shop.sid.equals(sid)) {
                        ds.getRef().removeValue();
                        Log.d("btag", "removed shop ref " + shop.sid);
                        ref.removeEventListener(this);
                        cb.call(true);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ref.removeEventListener(this);
            }
        });
    }

    public static void getItem(final String iid, final Callback<Item> cb) {
        Log.d("btag", "getItem iid " + iid);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.child("items").getChildren()) {
                    Item item = ds.getValue(Item.class);
                    Log.d("btag", "item name " + item.name);
                    Log.d("btag", "item iid " + item.iid);
                    Log.d("btag", "item sid " + item.sid);
                    Log.d("btag", "item description " + item.description);
                    Log.d("btag", "item category " + item.category);
                    Log.d("btag", "item price " + item.price);
                    Log.d("btag", "item quantity " + item.quantity);
//                    Log.d("btag", "buys " + item.buys);
                    Log.d("btag", "color" + item.variation.get("color"));
                    Log.d("btag", "size" + item.variation.get("size"));

                    if (item.iid.equals(iid)) {
                        Log.d("btag", "found item iid " + iid);
                        cb.call(item);
                        ref.removeEventListener(this);
                        return;
                    }
                }
                cb.call(null);
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ref.removeEventListener(this);
            }
        });
    }
    public static void deleteItem(final String iid, final Callback<Boolean> cb) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("items").getChildren()) {
                    Item item = ds.getValue(Item.class);
                    if (item.iid.equals(iid)) {
                        ds.getRef().removeValue();
                        Log.d("btag", "removed item ref " + item.iid);
                        ref.removeEventListener(this);
                        cb.call(true);
                        return;
                    }
                }
                cb.call(false);
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ref.removeEventListener(this);
            }
        });
    }
}

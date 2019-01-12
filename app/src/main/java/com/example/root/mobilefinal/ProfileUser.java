package com.example.root.mobilefinal;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileUser extends Fragment {


    private FragmentActivity myContext;
    private ViewPager pager;
    private TabLayout tabLayout;
    CircleImageView imageView;
    FirebaseStorage storage;
    Bitmap bitmap_avatar;
    TextView textView_username;
    FirebaseUser currentUser;

    public ProfileUser() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_user, container, false);
        addControl(view);
        setUpFirebase();
        setData();
        return view;
    }

    private void setData() {
        textView_username.setText(getUserNameFromEmail(currentUser.getEmail()));
        downloadAvatar();

    }

    void setUpFirebase() {
        storage = FirebaseStorage.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void addControl(View view) {
        pager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        FragmentManager manager = myContext.getSupportFragmentManager();
        PageAdapter adapter = new PageAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        imageView = view.findViewById(R.id.profile_image);
        textView_username = view.findViewById(R.id.tv_username);
    }

    String getUserNameFromEmail(String email) {
        return email.split("@")[0];
    }

    private void downloadAvatar() {
        if (bitmap_avatar == null) {
            bitmap_avatar = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
            imageView.setImageBitmap(bitmap_avatar);
            StorageReference ref = storage.getReference("avatar/" + getUserNameFromEmail(currentUser.getEmail()) + ".jpg");
            ref.getBytes(1 << 20).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.i("btag", "byte length " + bytes.length);
                    bitmap_avatar = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.i("btag", "avatar download succeed " + currentUser.getEmail());
                    imageView.setImageBitmap(bitmap_avatar);
                    imageView.invalidate();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("btag", "avatar download failed " + currentUser.getEmail());
                }
            });
        }
    }
}

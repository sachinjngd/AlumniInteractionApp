package com.example.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView main_recycler_view;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView img_logout , img_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        auth = FirebaseAuth.getInstance() ;
        database = FirebaseDatabase.getInstance();
        usersArrayList= new ArrayList<>();

        DatabaseReference databaseReference = database.getReference().child("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users user = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        main_recycler_view = findViewById(R.id.main_recycler_view);
        img_logout  = findViewById(R.id.img_logout);
        img_setting = findViewById(R.id.img_setting);
        main_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(HomeActivity.this , usersArrayList);
        main_recycler_view.setAdapter(adapter);


        if(auth.getCurrentUser() == null){

            startActivity(new Intent(HomeActivity.this , RegistrationActivity.class));

        }

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(HomeActivity.this , R.style.Dialog);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yes_btn , no_btn;

                yes_btn = dialog.findViewById(R.id.yes_btn);
                no_btn = dialog.findViewById(R.id.no_btn);

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this , LoginActivity.class));
                    }
                });

                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this , SettingActivity.class));
            }
        });

    }
}
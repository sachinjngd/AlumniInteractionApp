package com.example.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String name , imageUri , uid;
    CircleImageView profile_pic;
    TextView user_name ;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth ;
    public static String sImage;
    public static String rImage ;
    EditText edt_message;
    CardView send_btn;
    String senderRoom , recieverRoom ;
    RecyclerView message_adapter;
    ArrayList<Messages> messagesArrayList ;
    MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesArrayList  = new ArrayList<>();
        profile_pic = findViewById(R.id.recievers_profile_image);
        user_name = findViewById(R.id.recievers_user_name);
        send_btn = findViewById(R.id.send_btn);
        edt_message = findViewById(R.id.edt_message);
        message_adapter = findViewById(R.id.message_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        message_adapter.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(ChatActivity.this,messagesArrayList);
        message_adapter.setAdapter(messagesAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();



        name = getIntent().getStringExtra("name");
        imageUri = getIntent().getStringExtra("imageUri");
        uid = getIntent().getStringExtra("uid");

        Picasso.get().load(imageUri).into(profile_pic);
        user_name.setText(""+name);

        senderRoom = auth.getUid() + uid ;
        recieverRoom = uid + auth.getUid();

        DatabaseReference reference = firebaseDatabase.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sImage =  snapshot.child("imageuri").getValue().toString();
                Log.d("sachin", "onDataChange: " + sImage);
                rImage =   imageUri;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message  = edt_message.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                    return;
                }
                edt_message.setText("");
                Date date = new Date();
                Messages messages  = new Messages(message , auth.getUid(),date.getTime() );

                firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                firebaseDatabase.getReference().child("chats")
                                        .child(recieverRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        });

            }
        });


    }
}
package com.example.tims_project.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.DatabaseViewModel;
import com.example.tims_project.LogInViewModel;
import com.example.tims_project.R;
import com.example.tims_project.adapter.AdapterChat;
import com.example.tims_project.model.ModelChat;
import com.example.tims_project.model.User;
import com.example.tims_project.model.notification.APIService;
import com.example.tims_project.model.notification.Client;
import com.example.tims_project.model.notification.Data;
import com.example.tims_project.model.notification.MyResponse;
import com.example.tims_project.model.notification.Sender;
import com.example.tims_project.model.notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {



    DatabaseViewModel databaseViewModel;
    LogInViewModel logInViewModel;
    RecyclerView recyclerView;



    TextView nameTv,userStatusTv1;
    EditText massageEt;
    ImageButton sendBtn;
    FirebaseAuth firebaseAuth;
    String hisUid;
    String myUid;
    String token;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDbref;
    String hisImage;
    FirebaseAuth mUser;

    ValueEventListener seenlistener;
    DatabaseReference userRefForSeen;
    List<ModelChat> chatList;
    AdapterChat adapterChat;

    APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        databaseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(DatabaseViewModel.class);

        logInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()))
                .get(LogInViewModel.class);


        recyclerView=findViewById(R.id.chat_recyclerView);

        nameTv=findViewById(R.id.nameTv1);
        massageEt=findViewById(R.id.messageET1);
        sendBtn=findViewById(R.id.sendIB);
        userStatusTv1=findViewById(R.id.userStatusTv1);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager((this));
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mUser = FirebaseAuth.getInstance();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        Intent intent=getIntent();
        hisUid=intent.getStringExtra("hisUid");
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseDatabase=FirebaseDatabase.getInstance();
        userDbref=firebaseDatabase.getReference("Friend").child(mUser.getUid());
        //search user to get their user info
        Query userQuery=userDbref.orderByChild("uid").equalTo(hisUid);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        myUid=user.getUid();

        //getTokens();



        //get user pic
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check until required is received
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name=""+ ds.child("name").getValue();


                    String typingStatus=""+ ds.child("typingTo").getValue();

                    if(typingStatus.equals(myUid)){
                        userStatusTv1.setText("typing..");
                    }else{
                        String onlineStatus=""+ ds.child("onlineStatus").getValue();
                        if(onlineStatus.equals("online")){
                            userStatusTv1.setText(onlineStatus);
                        }else{

//                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());
//                            String dateTime = simpleDateFormat1.format(new Date(Long.parseLong(onlineStatus)));
//                            userStatusTv1.setText("Last seen at :"+dateTime);
                        }

                    }

                    //set data typingTo/////aaaaaaaaa
                    nameTv.setText(name);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        //String msgt = getString(R.string.msg_token_fmt, token);
                        HashMap hashMap = new HashMap();


                        hashMap.put("token",token);

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");

                        ref.child(user.getUid()).updateChildren(hashMap);
                        Log.d("token "," "+token);

                        //request.child("Request").child(uid).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        //                        @Override
                        //                        public void onComplete(@NonNull Task task) {
                        //
                        //                            if(task.isSuccessful()){
                        //                                currentState = "sent_req";
                        //                                holder.sendBt.setText("Cancel");
                        //                                Toast.makeText(context,""+holder.sendBt.getText().toString(),Toast.LENGTH_SHORT).show();
                        //
                        //
                        //                            }
                        //                        }
                        //                    });

                    }
                });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message=massageEt.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this,"Cannot send the empty message",Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMessage(message);
                }

                massageEt.setText("");

            }
        });
        massageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    checkTypingStatus("noOne");
                }
                else{
                    checkTypingStatus(hisUid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        readMessages();
        seenMessages();



    }

    private void seenMessages() {
        userRefForSeen=FirebaseDatabase.getInstance().getReference("Chats");
        seenlistener=userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)){
                        HashMap<String,Object> hasseenHashMap=new HashMap<>();
                        hasseenHashMap.put("isSeen",true);
                        ds.getRef().updateChildren(hasseenHashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessages(){
        chatList=new ArrayList<>();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid)&&chat.getSender().equals(hisUid)||
                            chat.getReceiver().equals(hisUid)&&chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }
                    adapterChat=new AdapterChat(ChatActivity.this,chatList,hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        String timestamp=String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("isSeen",false);
        databaseReference.child("Chats").push().setValue(hashMap);


        String msg = message;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Friend").child(myUid).child(hisUid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;

                if(notify){
                    sendNotification(hisUid,user.getName(),msg);
                    Log.d("my uid "," "+myUid);
                    Log.d("sent notification"," "+hisUid +" toke"+user.getName()+"his "+msg);
                }
                notify = false;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




//    private void sendNotification(String hisUid, String username, String msg) {
//        databaseViewModel.getTokenDatabaseRef();
//        databaseViewModel.getTokenRefDb.observe(this, new Observer<DatabaseReference>() {
//            @Override
//            public void onChanged(DatabaseReference databaseReference) {
//                Query query = databaseReference.orderByKey().equalTo(hisUid);
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                           // Token token = snapshot.getValue(Token.class);
//
//
//                            Data data = new Data(myUid, String.valueOf(R.drawable.ic_baseline_textsms_24), username + ": " + msg, "New Message", hisUid);
//
//                         //   assert token != null;
//                            Sender sender = new Sender(data, token);
//                           // Log.d("token"," token"+token.getToken());
//
//                            apiService.sendNotification(sender)
//                                    .enqueue(new Callback<MyResponse>() {
//                                        @Override
//                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                            if (response.code() == 200) {
//                                                assert response.body() != null;
//                                                if (response.body().success != 1) {
//                                                    Log.d("noto "," sendd");
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<MyResponse> call, Throwable t) {
//
//                                        }
//                                    });
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
//    }

    private void sendNotification(String hisUid, String name, String msg) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");

        Log.d("notification"," ttttttttttttttttt");

        Query query = allTokens.orderByKey().equalTo(hisUid);

//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds:snapshot.getChildren()){
                   // Token token = ds.getValue(Token.class);
                    Log.d("notificationoooo"," tttttttttttttttttpppp");
                    String tokent=allTokens.child(hisUid).child("token").toString();
                  //  String user, String icon, String body, String title, String sent
                    Data data = new Data(myUid,name+":"+msg,"New Message",hisUid, String.valueOf(R.drawable.ic_baseline_textsms_24));
                    Log.d("notification"," "+myUid +" toke "+tokent+"his "+hisUid);
                    Log.d("appppp"," appppiii");


                    Sender sender = new Sender(data,tokent);
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {

                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        Log.d("appp88pp"," app88ppiii");
                                        assert response.body() != null;
                                        if (response.body().success != 1) {
                                            Log.d("chat"," succ");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.d("notification"," eee");

                                }
                            });
            //    }
          //  }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            myUid=user.getUid();

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("currentuser", myUid);
            editor.apply();

        }else{
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }

    private void checkOnlineStatus(String status){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("onlineStatus",status);//onlineStatus
        dbRef.updateChildren(hashMap);
    }
    private void checkTypingStatus(String typing){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("typingTo",typing);//typingTo
        dbRef.updateChildren(hashMap);
    }




    @Override
    protected void onPause() {
        super.onPause();

        //get timestampp
        String timestamp=String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");

        userRefForSeen.removeEventListener(seenlistener);
    }

    @Override
    protected void onResume() {

        checkOnlineStatus("online");

        super.onResume();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        checkOnlineStatus("online");
        super.onStart();
    }





}
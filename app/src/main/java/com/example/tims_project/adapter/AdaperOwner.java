package com.example.tims_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.R;
import com.example.tims_project.SendReq;
import com.example.tims_project.model.ModelUser;
import com.example.tims_project.ui.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AdaperOwner extends RecyclerView.Adapter<AdaperOwner.MyHolder> {
    Context context;
    List<ModelUser> userList;
    String currentState = "nothing";
    DatabaseReference request;

    FirebaseAuth mUser;
    String name;



    public AdaperOwner(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_owner,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String userName=userList.get(position).getFullName();
        final String userEmail=userList.get(position).getUserEmail();
        final String uid=userList.get(position).getUid();
        request = FirebaseDatabase.getInstance().getReference();


        mUser = FirebaseAuth.getInstance();



        holder.mName.setText(userName);
        holder.mEmail.setText(userEmail);
        holder.sendBt.setText("Send");
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in =new Intent(context,ProfileActivity.class);
//                in.putExtra("userKey",mUser.getCurrentUser());
//                context.startActivity(in);
//            }
//        });
        request.child("Tenant").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name=snapshot.child("FullName").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(currentState.equals("nothing")){
                    HashMap hashMap = new HashMap();


                    hashMap.put("uid",mUser.getUid());
                    hashMap.put("status","pending");
                    hashMap.put("name", name);
                    hashMap.put("email",mUser.getCurrentUser().getEmail());

                    request.child("Request").child(uid).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if(task.isSuccessful()){
                                currentState = "sent_req";
                                holder.sendBt.setText("Cancel");
                                Toast.makeText(context,""+holder.sendBt.getText().toString(),Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }
                if(currentState.equals("sent_req")||currentState.equals("decline")){
                    request.child("Request").child(mUser.getUid()).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if(task.isSuccessful()){
                                currentState = "nothing";
                                holder.sendBt.setText("Send");
                                Toast.makeText(context,""+holder.sendBt.getText().toString(),Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                }
//                if(currentState.equals("he_sent_pending")){
//                    request.child("Request").child(uid).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                HashMap hashMap = new HashMap();
//                                hashMap.put("uid",mUser.getUid());
//                                hashMap.put("status","friend");
//                                hashMap.put("name", name);
//                                hashMap.put("email",mUser.getCurrentUser().getEmail());
//
//                                request.child("Friend").child(uid).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(
//                                        new OnCompleteListener() {
//                                            @Override
//                                            public void onComplete(@NonNull Task task) {
//                                                if(task.isSuccessful()){
//                                                    request.child("Friend").child(mUser.getUid()).child(uid).updateChildren(hashMap).addOnCompleteListener(
//                                                            new OnCompleteListener() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task task) {
//
//                                                                    currentState="friend";
//
//                                                                }
//                                                            }
//                                                    );
//                                                }
//
//
//                                            }
//                                        }
//                                );
//
//                            }
//
//                        }
//                    });
//                }


            }
        });


    }

    private void CheckUserExistance(String uid) {
        request.child("Friend").child(uid).child(mUser.getUid())
               .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           currentState="friend";
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

        request.child("Request").child(mUser.getUid()).child(uid).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("status").getValue().toString().equals("pending")){
                            currentState = "i_sent_pending";
                        }
                        if(snapshot.child("status").getValue().toString().equals("pending")){
                            currentState = "i_sent_decline";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }




    @Override
    public int getItemCount() {
        if (userList==null || userList.size()==0){
            return 0;
        }else{
            return userList.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView mName,mEmail;
        Button sendBt;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mName=itemView.findViewById(R.id.nameTv);
            mEmail=itemView.findViewById(R.id.emailTv);
            sendBt=itemView.findViewById(R.id.sentBt);
        }
    }

}

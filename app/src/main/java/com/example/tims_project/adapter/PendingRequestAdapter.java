package com.example.tims_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.AddTen;
import com.example.tims_project.R;
import com.example.tims_project.model.RequestModel;
import com.example.tims_project.ui.ChatActivity;
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

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.MyHolder>{

    Context context;
    List<RequestModel> userList;

    DatabaseReference request;
    FirebaseAuth mUser;
    String name,email;
    String current = "nothing";

    public PendingRequestAdapter(Context context, List<RequestModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_owner,parent,false);
        return new PendingRequestAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String userName=userList.get(position).getName();
        final String userEmail=userList.get(position).getEmail();
       final String uid=userList.get(position).getUid();
//        request = FirebaseDatabase.getInstance().getReference().child("Request");
//        mUser = FirebaseAuth.getInstance();
        holder.mName.setText(userName);
        holder.mEmail.setText(userEmail);
        request = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,""+ userName,Toast.LENGTH_SHORT).show();

            }
        });

        request.child("Owner").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name=snapshot.child("FullName").getValue().toString();
                    email=snapshot.child("UserEmail").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    request.child("Request").child(mUser.getUid()).child(uid).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        HashMap hashMap = new HashMap();
                                        hashMap.put("uid", uid);
                                        hashMap.put("status", "friend");
                                        hashMap.put("name", userName);
                                        hashMap.put("email", userEmail);


                                        request.child("Friend").child(mUser.getUid()).child(uid).updateChildren(hashMap).addOnCompleteListener(
                                                new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, "Added Success", Toast.LENGTH_SHORT).show();
                                                            current = "success";
                                                        }


                                                    }
                                                }
                                        );


                                        HashMap hashMap1 = new HashMap();
                                        hashMap1.put("uid", mUser.getUid());
                                        hashMap1.put("status", "friend");
                                        hashMap1.put("name", name);
                                        hashMap1.put("email", email);

                                        request.child("Friend").child(uid).child(mUser.getUid()).updateChildren(hashMap1).addOnCompleteListener(
                                                new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, "Added Success", Toast.LENGTH_SHORT).show();

                                                            current = "success";


                                                        }


                                                    }
                                                }
                                        );
                                    request.child("Request").child(mUser.getUid()).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){


                                            }
                                        }
                                    });



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }
                    );



            }
        });
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

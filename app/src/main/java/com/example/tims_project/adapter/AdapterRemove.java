package com.example.tims_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.R;
import com.example.tims_project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterRemove extends RecyclerView.Adapter<AdapterRemove.MyHolder> {
    Context context;
    List<User> userList;
    DatabaseReference request;
    FirebaseAuth mUser;

    public AdapterRemove(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdapterRemove.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_chat_remove,parent,false);
        return new AdapterRemove.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String uid=userList.get(position).getUid();
        String userName=userList.get(position).getName();
        final String userEmail=userList.get(position).getEmail();
        mUser = FirebaseAuth.getInstance();

        holder.mName.setText(userName);
        holder.mEmail.setText(userEmail);
        request = FirebaseDatabase.getInstance().getReference();
        holder.mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                request.child("Friend").child(mUser.getUid()).child(uid).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                request.child("Friend").child(mUser.getUid()).child(uid).removeValue().addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context,"Removed",Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


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
    //nnn
    class MyHolder extends RecyclerView.ViewHolder{

        TextView mName,mEmail;
        Button mbutton;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.RnameTv);
            mEmail=itemView.findViewById(R.id.RemailTv);
            mbutton=itemView.findViewById(R.id.RsentBt);
        }
    }
}


package com.example.tims_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.R;
import com.example.tims_project.model.ModelUser;
import com.example.tims_project.model.RequestModel;
import com.example.tims_project.model.User;
import com.example.tims_project.ui.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {
    Context context;
    List<User> userList;

    public AdapterUsers(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String hisUID=userList.get(position).getUid();

        String userName=userList.get(position).getName();
        final String userEmail=userList.get(position).getEmail();

        holder.mName.setText(userName);
        holder.mEmail.setText(userEmail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUID);
                context.startActivity(intent);

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

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.mnameTv);
            mEmail=itemView.findViewById(R.id.memailTv);
        }
    }
}


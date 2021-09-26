package com.example.tims_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.R;
import com.example.tims_project.uploadedpdf;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterRemoveNotice extends RecyclerView.Adapter<AdapterRemoveNotice.MyHolder> {
    Context context;
    List<uploadedpdf> userList;
    DatabaseReference request;
    FirebaseAuth mUser;
    DatabaseReference databaseReference;
    String tt;

    public AdapterRemoveNotice(Context context, List<uploadedpdf> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdapterRemoveNotice.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_remove_notice,parent,false);
        return new AdapterRemoveNotice.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRemoveNotice.MyHolder holder, int position) {

        String userName=userList.get(position).getName();
        String pdflink=userList.get(position).getUrl();
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
        mUser = FirebaseAuth.getInstance();

        databaseReference.child(mUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()) {
                            tt=ds.getKey().toString();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.mName.setText(userName);
       // holder.mPdfLink.setText(pdflink);
        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{




                    databaseReference.child(mUser.getUid()).child(tt).removeValue().addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Remove Notice", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });


            } catch (ArrayIndexOutOfBoundsException e) {
                //System.out.println(e);
                    Toast.makeText(context,"No Data Found",Toast.LENGTH_SHORT).show();

                }


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

        TextView mName,mPdfLink;
        ImageView mRemove;



        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.mpdf_name);
            //mPdfLink=itemView.findViewById(R.id.mpdf_link);
            mRemove=itemView.findViewById(R.id.mremove);

        }
    }
}


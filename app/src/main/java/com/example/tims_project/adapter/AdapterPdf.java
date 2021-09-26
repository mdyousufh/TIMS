package com.example.tims_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.R;
import com.example.tims_project.uploadedpdf;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class AdapterPdf extends RecyclerView.Adapter<AdapterPdf.MyHolder> {
    Context context;
    List<uploadedpdf> userList;
    DatabaseReference request;
    FirebaseAuth mUser;

    public AdapterPdf(Context context, List<uploadedpdf> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AdapterPdf.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_pdf,parent,false);
        return new AdapterPdf.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String userName=userList.get(position).getName();
        String pdflink=userList.get(position).getUrl();


        holder.mName.setText(userName);
        //holder.mPdfLink.setText(pdflink);

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



        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.pdf_name);
           // mPdfLink=itemView.findViewById(R.id.pdf_link);

        }
    }
}


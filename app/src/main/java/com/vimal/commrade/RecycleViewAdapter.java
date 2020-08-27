package com.vimal.commrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList id,nameofMember,relationwithUser,mobileofMember;
    RecycleViewAdapter(Context context,ArrayList id,ArrayList nameofMember,ArrayList relationwithUser,ArrayList mobileofMember){
        this.context=context;
        this.id=id;
        this.nameofMember=nameofMember;
        this.relationwithUser=relationwithUser;
        this.mobileofMember=mobileofMember;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.member_row,parent,false);
        return  new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Id.setText(String.valueOf(id.get(position)));
        holder.Name.setText(String.valueOf(nameofMember.get(position)));
        holder.Mobile.setText(String.valueOf(mobileofMember.get(position)));


    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Id,Name,Mobile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Id=itemView.findViewById(R.id.member_id);
            Name=itemView.findViewById(R.id.textViewName);
            Mobile=itemView.findViewById(R.id.textViewMobile);

        }
    }
}

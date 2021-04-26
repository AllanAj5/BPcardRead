package com.example.bpcardread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpcardread.EntityClass.UserModel;

import java.util.List;

public class DetailsListAdapter extends RecyclerView.Adapter<DetailsListAdapter.MyViewHolder> {
    private Context context;
    private List<UserModel> userList;

    public DetailsListAdapter(Context context) {
        this.context = context;
    }

    public void setUserList(List<UserModel> userList){
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(this.userList.get(position).name);
        //holder.dob.setText(this.userList.get(position).dob);
        //holder.sex.setText(this.userList.get(position).sex);
        holder.id.setText(this.userList.get(position).idNumber);
        //holder.addressu.setText(this.userList.get(position).address);

    }


    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    //Creating View Holder Class
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,dob,sex,id,addressu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            //dob = itemView.findViewById(R.id.dob);
            //sex = itemView.findViewById(R.id.sex);
            id = itemView.findViewById(R.id.id);
            //addressu = itemView.findViewById(R.id.address);


        }
    }
}

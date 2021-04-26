//package com.example.bpcardread.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.bpcardread.EntityClass.UserModel;
//import com.example.bpcardread.R;
//
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
//{
//    Context context;
//    List<UserModel> list;
//
//    public UserAdapter(Context context, List<UserModel> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        holder.name.setText(list.get(position).getName());
//        holder.dob.setText(list.get(position).getDob());
//        holder.sex.setText(list.get(position).getSex());
//        holder.id.setText(list.get(position).getIdNumber());
//        holder.address.setText(list.get(position).getAddress());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView name,dob,sex,id,address;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            name = itemView.findViewById(R.id.name);
//            dob = itemView.findViewById(R.id.dob);
//            sex = itemView.findViewById(R.id.sex);
//            id = itemView.findViewById(R.id.id);
//            address = itemView.findViewById(R.id.address);
//
//
//
//        }
//    }
//}

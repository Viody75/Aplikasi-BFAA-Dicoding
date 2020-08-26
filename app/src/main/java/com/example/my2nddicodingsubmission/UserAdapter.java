package com.example.my2nddicodingsubmission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ListViewHolder> {

    private ArrayList<UserModel> listUser;
    private Context context;

    //masih testing
//    public void setData(ArrayList<UserModel> items) {
//        listUser.clear();
//        listUser.addAll(items);
//        notifyDataSetChanged();
//    }

    UserAdapter(ArrayList<UserModel> list, Context context){
        this.listUser=list;
        this.context=context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent,false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final UserModel user = listUser.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.userPhoto);
        holder.name.setText(user.getUsername());
        holder.username.setText(user.getId());
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentDetail = new Intent(context, DetailUserActivity.class);
                intentDetail.putExtra("username", user.getUsername());
                context.startActivity(intentDetail);

                FollowerFragment followerFragment = new FollowerFragment();
                Bundle myBundle = new Bundle();
                myBundle.putString("usernameFrag", user.getUsername());
                followerFragment.setArguments(myBundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView name;
        TextView username;
        View detail;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.img_item_photo);
            username = itemView.findViewById(R.id.tv_rv_username);
            name = itemView.findViewById(R.id.tv_rv_name);
            detail = itemView.findViewById(R.id.to_detail);

        }
    }
}

package com.example.testret.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testret.Models.Contact;
import com.example.testret.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint({"CheckResult", "UseCompatLoadingForDrawables"})
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.HourlyHolder> {

    private List<Contact> contactList = new ArrayList<>();
    private final Context context;

    public ContactAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourlyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull HourlyHolder holder, int position) {
        holder.contactName.setText(contactList.get(position).getNom());
        holder.contactPhone.setText(contactList.get(position).getTelephone());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }


    public class HourlyHolder extends RecyclerView.ViewHolder {

        private final TextView contactName;
        private final TextView contactPhone;

        public HourlyHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
        }
    }
}
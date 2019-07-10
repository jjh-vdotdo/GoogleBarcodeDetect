package com.example.googlebarcodedetect.Recyclerview_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlebarcodedetect.Interface.ItemClickListener;
import com.example.googlebarcodedetect.R;
import com.example.googlebarcodedetect.Recyclerview_model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite RecyclerView Adapter클래스
 * push test
 *
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactHolder> {

    private List<Contact> contactList;
    private Context context;

    private ItemClickListener itemClickListener;

    public ContactListAdapter(Context c){
        this.context = c;
        this.contactList = new ArrayList<>();
    }

    private void add(Contact item){
        contactList.add(item);
        notifyItemInserted(contactList.size() -1);
    }

    public void addAll(List<Contact> contactList){
        for (Contact contact : contactList){
            add(contact);
        }
    }

    public void remove(Contact item){
        int position = contactList.indexOf(item);
        if (position > -1) {
            contactList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear(){
        while (getItemCount() > 0){
            remove(getItem(0));
        }
    }

    public Contact getItem(int position){
        return contactList.get(position);
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sqlite_list_item, parent, false);

        final ContactHolder contactHolder = new ContactHolder(view);

        contactHolder.itemView.setOnClickListener(new View.OnClickListener() { // 특정 아이템을 클릭했을 시 행동
            @Override
            public void onClick(View v) {
                int position = contactHolder.getAdapterPosition();
                if (position !=RecyclerView.NO_POSITION){
                    if (itemClickListener !=null){
                        itemClickListener.onItemClick(contactHolder.content, position);
                    }
                }
            }
        });

        return contactHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        final Contact contact = contactList.get(position);

        holder.name.setText(contact.getName());
        holder.content.setText(contact.getContent());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setItemClickListener(ItemClickListener clickListener){
        this.itemClickListener = clickListener;
    }

    public class ContactHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView content;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            content = (TextView)itemView.findViewById(R.id.Contents);
        }
    }
}

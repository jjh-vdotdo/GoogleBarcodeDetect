package com.example.googlebarcodedetect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.googlebarcodedetect.Interface.ItemClickListener;
import com.example.googlebarcodedetect.Recyclerview_Adapter.ContactListAdapter;
import com.example.googlebarcodedetect.Recyclerview_model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite DataBase를 이용해 데이터를 저장, 수정, 삭제하는 클래스
 * 주 기능 1. 저장된 모든 데이터를 리사이클러뷰에 출력한다
 *         2. 플로팅 버튼(fb)을 통해 새로운 데이터를 저장, 수정, 삭제하는 클래스(SQLite_Act_Activity.class)로 이동
 */

public class SQLite_Activity extends AppCompatActivity implements ItemClickListener {

    String TAG = "SQLite_Activity";

    private RecyclerView recyclerView;
    private FloatingActionButton fb;

    private ContactListAdapter contactListAdapter;
    private LinearLayoutManager layoutManager;

    private SQLiteDB sqLiteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_);

        recyclerView = (RecyclerView)findViewById(R.id.sqlite_recyclerview);
        fb = (FloatingActionButton)findViewById(R.id.fbBtn);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite_Act_Activity.start(SQLite_Activity.this);
            }
        });

        layoutManager = new LinearLayoutManager(this);
        contactListAdapter = new ContactListAdapter(this);
        contactListAdapter.setItemClickListener(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactListAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadData();
    }

    void loadData(){
        sqLiteDB = new SQLiteDB(this);

        List<Contact> contactList = new ArrayList<>();
        Cursor cursor = sqLiteDB.retrieve();
        Contact contact;

        if (cursor.moveToFirst()){ // Cursor를 통해 가져온 데이터중 첫번째로 이동 후 차례로 contactList에 쌓는다
            do {
                contact = new Contact();

                contact.setId(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setContent(cursor.getString(2));

                contactList.add(contact);
            }while (cursor.moveToNext());
        }
        contactListAdapter.clear();
        contactListAdapter.addAll(contactList);
        contactListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, int pos) {
        SQLite_Act_Activity.start(this, contactListAdapter.getItem(pos));
    }
}

package com.example.googlebarcodedetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.googlebarcodedetect.Recyclerview_model.Contact;

/**
 * SQLite DataBase로 데이터를 추가, 수정, 삭제하는 Activity
 * 새로운 데이터 입력인 경우 add버튼만 출력, 기존 데이터를 클릭했을 경우 edit, delete버튼만 출력
 */

public class SQLite_Act_Activity extends AppCompatActivity implements View.OnClickListener{

    private EditText name, contents;
    private Button add, edit, delete;

    private SQLiteDB sqLiteDB;
    private Contact contact;

    public static void start(Context context){ //새로운 데이터 입력의 경우 액티비티 이동만 실행
        Intent intent = new Intent(context, SQLite_Act_Activity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, Contact contact){ // 기존 데이터 클릭 시 클릭한 아이템의 데이터(이름, 내용)을 intent Extras에 담아 전달
        Intent intent = new Intent(context, SQLite_Act_Activity.class);
        intent.putExtra(SQLite_Act_Activity.class.getSimpleName(), contact);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite__act_);

        name = (EditText)findViewById(R.id.personText);
        contents = (EditText)findViewById(R.id.contentText);
        add = (Button)findViewById(R.id.btnAdd);
        edit = (Button)findViewById(R.id.btnEdit);
        delete = (Button)findViewById(R.id.btnDelete);

        add.setOnClickListener(this);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);

        contact = getIntent().getParcelableExtra(SQLite_Act_Activity.class.getSimpleName());

        if (contact != null){
            add.setVisibility(View.GONE);

            name.setText(contact.getName());
            contents.setText(contact.getContent());
        }else {
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        sqLiteDB = new SQLiteDB(this);
    }

    @Override
    public void onClick(View v) {
        if (v == add){
            contact = new Contact();
            contact.setName(name.getText().toString());
            contact.setContent(contents.getText().toString());
            sqLiteDB.add(contact);
//            sqLiteDB.create(contact);

            Toast.makeText(this, "Insert", Toast.LENGTH_SHORT).show();
            finish();
        }else if (v == edit){
            contact.setName(name.getText().toString());
            contact.setContent(contents.getText().toString());
            sqLiteDB.update(contact);

            Toast.makeText(this,"Update", Toast.LENGTH_SHORT).show();
            finish();
        }else if (v == delete){
            sqLiteDB.delete(contact.getId());

            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

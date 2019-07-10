package com.example.googlebarcodedetect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.googlebarcodedetect.Recyclerview_model.Contact;

/**
 * SQLite DataBase생성, 데이터 저장, 수정, 삭제 쿼리를 관리하는 클래스
 * @method add = 새로운 데이터를 insert 한다
 * @method retrieve = 기존 데이터를 select 한다
 * @method update = 기존 데이터 값을 update 한다
 * @method delete = 선택한 아이템에 해당하는 데이터를 delete 한다
 */

class SQLiteDB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SQdb";

    public static final String TABLE_NAME = "test_tb"; // 테이블 명
    public static final String COLUMN_ID = "id"; // id라는 컬럼명
    public static final String COLUMN_NAME = "name"; // name 이라는 컬럼명
    public static final String COLUMN_CONTENT = "content"; // content라는 컬럼명

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CONTENT + TEXT_TYPE + " )";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // 세번째 인수 factory는 Cursor를 이용할 경우 null로 지정한다.
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // DB가 처음 만들어 질 때 호출, 여기서 테이블 생성 및 레코드 삽입
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // DB를 업그레이드 할 때 호출(DB Version이 바뀌는 경우를 말함)
        db.execSQL(SQL_CREATE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean add(Contact contact){ // SQLite_Activity에서 넘겨 받은 데이터를 insert 해준다.
        try {
            SQLiteDatabase db = getWritableDatabase(); // 데이터를 쓰고 읽을 때 호출

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, contact.getName());
            values.put(COLUMN_CONTENT, contact.getContent());

            db.insert(TABLE_NAME, COLUMN_ID, values);

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Cursor retrieve(){ // DB의 query 결과는 Cursor라는 pointer의 개념의 object로 받아온다
        SQLiteDatabase db = getReadableDatabase(); // 데이터를 읽을 때 호출

        String[] columns = { // 가져오려는 데이터들의 위치(컬럼)을 정의
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_CONTENT };

        // 데이터를 어떤 기준으로 가져오고 싶은지 정의(sortOrder의 경우
        // name 컬럼을 기준으로 순차정렬된 데이터를 가져온다.)
        String sortOrder =
                COLUMN_NAME + " ASC";

        Cursor c = db.query(
                TABLE_NAME, // 테이블명
                columns, // 컬럼명
                null, // Where 절을 위한 컬럼명
                null, // Where절을 위한 벨류
                null, // 나누고자 하는 그룹의 컬럼명
                null, // 집계함수를 이용해 조건비교를 할 때 사용
                null // 정렬 기준
        );

        return c;
    }

    public void update(Contact contact){ // SQLite_Activity에서 넘겨 받은 데이터를 update 해준다.
        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_CONTENT, contact.getContent());

        String selection = COLUMN_ID + " LIKE ?"; // 어떤 행의 데이터를 업데이트 할것인지 id값으로 찾는다
        String[] selectionArgs = { String.valueOf(contact.getId()) };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){ // SQLite_Activity에서 넘겨 받은 데이터(id값)를 이용해 delete 해준다
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_ID + " LIKE ?"; // 어떤 데이터를 지울것인지 id로 판별
        String[] selectionArgs = { String.valueOf(id) };

        db.delete(TABLE_NAME, selection, selectionArgs);
    }


}

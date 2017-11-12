package com.example.isolatorv.wipi.Utils;

/**
 * Created by tlsdm on 2017-11-13.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlsdm on 2017-11-10.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "test";
    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE FEED_TABLE( ");
        sb.append("_ID INTEGER,");
        sb.append("_PUSH TEXT,");
        sb.append("_NAME TEXT,");
        sb.append("_DATE TEXT,");
        sb.append("_DAY TEXT,");
        sb.append("_COUNT TEXT)");

        StringBuffer sb2 = new StringBuffer();
        sb2.append("CREATE TABLE TIME_TABLE( ");
        sb2.append("_ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb2.append(" TIME TEXT)");
        //SQLITE Database 로 쿼리 실행

        StringBuffer sb3 = new StringBuffer();
        sb3.append("CREATE TABLE MINGUE_TABLE(");
        sb3.append("_ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb3.append("_NAME TEXT,");
        sb3.append("_AGE TEXT)");

        db.execSQL(sb.toString());
        db.execSQL(sb2.toString());
        db.execSQL(sb3.toString());
        Log.d(TAG,"DBHelper : db생성 완료");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG,"버전이 올라갔습니다.");
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addMingue(String name,String age){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb =new StringBuffer();
        sb.append("INSERT INTO MINGUE_TABLE(");
        sb.append("_NAME,_AGE)");
        sb.append("VALUES (?,?)");

        db.execSQL(sb.toString(),
                new Object[]{
                        name,
                        age
                });

        Log.d(TAG,"DBHelper-MINGUE : db Insert 완료");
    }

    public List getAllMINGUEData(){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT _ID,_NAME,_AGE FROM MINGUE_TABLE");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(),null);
        List Mingu = new ArrayList();

        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            Mingu.add(id);
            Mingu.add(name);
            Mingu.add(age);
        }
        Log.d(TAG,"DBHelper-Mingu : db 불러오기 완료");
        return Mingu;
    }
    public void addTime(String time){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TIME_TABLE(");
        sb.append("TIME)");
        sb.append("VALUES (?)");

        db.execSQL(sb.toString(),
                new Object[]{
                        time
                });

        Log.d(TAG,"DBHelper-time : db Insert 완료");
    }

    public List getAllTimeData() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT  _ID, TIME  FROM TIME_TABLE ");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List timearry = new ArrayList();

        while (cursor.moveToNext()) {
            String time = cursor.getString(1);
            timearry.add(time);
        }
        Log.d(TAG,"DBHelper-time : db 불러오기 완료");
        return timearry;
    }

    public void upDatetime(String time,int id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TIME",time);
        db.update("TIME_TABLE",values,"_Id=?",new String[]{String.valueOf(id)});
        Log.d(TAG,"DBHelper-time : db 업데이트 완료");
    }

    public void addFeed(String id,String push,String name,String date,String day,int count){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO FEED_TABLE(");
        sb.append("_ID,_PUSH,_NAME,_DATE,_DAY,_COUNT)");
        sb.append("VALUES (?,?,?,?,?,?)");

        db.execSQL(sb.toString(),
                new Object[]{
                        id,
                        push,
                        name,
                        date,
                        day,
                        count
                });

        Log.d(TAG,"DBHelper-feed : db Insert 완료");
    }

    public List getAllFeedData(int _id){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT _ID ,_PUSH,_NAME,_DATE,_DAY,_COUNT FROM FEED_TABLE WHERE _ID = ");
        sb.append(_id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List feedarry = new ArrayList();

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String push = cursor.getString(1);
            String name = cursor.getString(2);
            String date = cursor.getString(3);
            String count = cursor.getString(4);
            String day = cursor.getString(5);
            feedarry.add(id);
            feedarry.add(push);
            feedarry.add(name);
            feedarry.add(date);
            feedarry.add(count);
            feedarry.add(day);
        }


        Log.d(TAG,"DBHelper-feedAlldata : db 불러오기 완료");
        return feedarry;
    }
    public List getFeedData(int _id,String str){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT _ID,_PUSH,_NAME,_DATE,_DAY,_COUNT FROM FEED_TABLE WHERE _ID = '");
        sb.append(_id);
        sb.append("'AND _DAY ='");
        sb.append(str);
        sb.append("'");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(),null);
        List feedarry = new ArrayList();
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String push = cursor.getString(1);
            String name = cursor.getString(2);
            String date = cursor.getString(3);
            String count = cursor.getString(4);
            String day = cursor.getString(5);
            feedarry.add(id);
            feedarry.add(push);
            feedarry.add(name);
            feedarry.add(date);
            feedarry.add(count);
            feedarry.add(day);
        }
        return feedarry;
    }

    public void upDatefeed(int id,int count){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_COUNT",count);
        db.update("FEED_TABLE",values,"_Id=?",new String[]{String.valueOf(id)});
        Log.d(TAG,"DBHelper-feed_count : db 업데이트 완료");
    }
    public void updatefeed2(int id,String ox){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_PUSH",ox);
        db.update("FEED_TABLE",values,"_ID=?",new String[]{String.valueOf(id)});
        Log.d(TAG,"DBHelper-feed_push : db 업데이트 완료");
    }
    public void deleteList(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("DELETE FROM %s WHERE %s = %d","FEED_TABLE","_Id",id));
        Log.d(TAG,"DBHelper_list : db 삭제 완료");
    }
}

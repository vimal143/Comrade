package com.vimal.commrade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DatabaseHandler extends SQLiteOpenHelper {

    private  Context context;
    private static final String DATABASE_NAME="Member.db";
    private static final String TABLE_NAME="Member";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_RELATION="relationship";
    private static final String COLUMN_MOBILE="mobile";

    private static final int DATABASE_VERSION=1;
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query= "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_RELATION + " TEXT, "+
                COLUMN_MOBILE + " TEXT);";
        sqLiteDatabase.execSQL(query);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

    void addFamilyMember(String name,String relation,String mobile){
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_RELATION,relation);
        contentValues.put(COLUMN_MOBILE,mobile);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Added Succesfully", Toast.LENGTH_SHORT).show();
        }
        db.close();

    }
    Cursor readData(){
        String query="SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(query,null);
        }
        return cursor;
    }

    void deleteDataofMember(String rowid){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{rowid});

    }
}

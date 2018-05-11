package com.example.administrator.italker.ui.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.administrator.common.widget.Medicine;

import java.util.ArrayList;

/**
 * Created by ZDY
 * on 2017/jinbao2/10
 */

public class DbHelper extends SQLiteOpenHelper{

    private static DbHelper mDbHelper;
    private static String MEDICINE_BOX = "create table medicine_box("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "detail text," +
            "useTime text," +
            "dosage text," +
            "taboo text," +
            "effect text)";
    private Context mContext;
    private Medicine medicine;
    private ArrayList<Medicine> mMedicines;

    public static DbHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        mDbHelper = new DbHelper(context,name,factory,version);
        return mDbHelper;
    }

    private DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MEDICINE_BOX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void add(SQLiteDatabase database , Medicine medicine){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",medicine.getName());
        contentValues.put("detail",medicine.getDetail());
        contentValues.put("useTime",medicine.getUseTime());
        contentValues.put("dosage",medicine.getDosage());
        contentValues.put("taboo",medicine.getTaboo());
        contentValues.put("effect",medicine.getEffect());
        database.insert("medicine_box",null,contentValues);
        contentValues.clear();
    }

    public void delete(SQLiteDatabase database, String name){
            database.delete("medicine_box",
                    "name = ?" ,new String[]{name});

    }

    public void update(SQLiteDatabase database,Medicine medicine){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",medicine.getName());
        contentValues.put("detail",medicine.getDetail());
        contentValues.put("useTime",medicine.getUseTime());
        contentValues.put("dosage",medicine.getDosage());
        contentValues.put("taboo",medicine.getTaboo());
        contentValues.put("effect",medicine.getEffect());
        database.update("medicine_box",contentValues,
                "name = ?",new String[]{medicine.getName()});
        contentValues.clear();
    }

    public Medicine queryByName(SQLiteDatabase database, String name){
        medicine = new Medicine();
        Cursor cursor = database.query("medicine_box",null,
                "name = ?",new String[]{name}
        ,null,null,null);
        if (cursor.moveToFirst()){
                String queryName = cursor.getString(cursor.getColumnIndex("name"));
            String detail = cursor.getString(cursor.getColumnIndex("detail"));
            String useTime = cursor.getString(cursor.getColumnIndex("useTime"));
            String dosage = cursor.getString(cursor.getColumnIndex("dosage"));
            String taboo = cursor.getString(cursor.getColumnIndex("taboo"));
            String effect = cursor.getString(cursor.getColumnIndex("effect"));
            medicine.setName(queryName);
            medicine.setDetail(detail);
            medicine.setUseTime(useTime);
            medicine.setDosage(dosage);
            medicine.setTaboo(taboo);
            medicine.setEffect(effect);

            return medicine;
        }
        return null;

    }
    public Medicine queryById(SQLiteDatabase database, int id){
        medicine = new Medicine();
        Cursor cursor = database.query("medicine_box",null,
                "id = ?",new String[]{String.valueOf(id)}
                ,null,null,null);
        if (cursor.moveToFirst()){
            String queryName = cursor.getString(cursor.getColumnIndex("name"));
            String detail = cursor.getString(cursor.getColumnIndex("detail"));
            String useTime = cursor.getString(cursor.getColumnIndex("useTime"));
            String dosage = cursor.getString(cursor.getColumnIndex("dosage"));
            String taboo = cursor.getString(cursor.getColumnIndex("taboo"));
            String effect = cursor.getString(cursor.getColumnIndex("effect"));
            medicine.setName(queryName);
            medicine.setDetail(detail);
            medicine.setUseTime(useTime);
            medicine.setDosage(dosage);
            medicine.setTaboo(taboo);
            medicine.setEffect(effect);

            return medicine;
        }
        return null;

    }
    public ArrayList<Medicine> queryByTime(SQLiteDatabase database, String time){
        mMedicines = new ArrayList<>();
        Cursor cursor = database.query("medicine_box",null,
                null,null
                ,null,null,null);

        if (cursor.moveToFirst()){
            do {
                medicine = new Medicine();
                String queryName = cursor.getString(cursor.getColumnIndex("name"));
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                String useTime = cursor.getString(cursor.getColumnIndex("useTime"));
                String dosage = cursor.getString(cursor.getColumnIndex("dosage"));
                String taboo = cursor.getString(cursor.getColumnIndex("taboo"));
                String effect = cursor.getString(cursor.getColumnIndex("effect"));
                if (useTime != null){
                    if (Integer.valueOf(useTime) >Integer.valueOf(time)){
                        medicine.setName(queryName);
                        medicine.setDetail(detail);
                        medicine.setUseTime(useTime);
                        medicine.setDosage(dosage);
                        medicine.setTaboo(taboo);
                        Log.d("medicine",useTime+time);
                        medicine.setEffect(effect);
                        mMedicines.add(medicine);
                    }
                }


            }while (cursor.moveToNext());

        }


        return mMedicines;

    }


    public ArrayList<Medicine> queryAll(SQLiteDatabase database){

        mMedicines = new ArrayList<>();
        Cursor cursor = database.query("medicine_box",null,
                null,null
                ,null,null,null);

            if (cursor.moveToFirst()){
                do {
                    medicine = new Medicine();
                    String queryName = cursor.getString(cursor.getColumnIndex("name"));
                    String detail = cursor.getString(cursor.getColumnIndex("detail"));
                    String useTime = cursor.getString(cursor.getColumnIndex("useTime"));
                    String dosage = cursor.getString(cursor.getColumnIndex("dosage"));
                    String taboo = cursor.getString(cursor.getColumnIndex("taboo"));
                    String effect = cursor.getString(cursor.getColumnIndex("effect"));
                    medicine.setName(queryName);
                    medicine.setDetail(detail);
                    medicine.setUseTime(useTime);
                    medicine.setDosage(dosage);
                    medicine.setTaboo(taboo);
                    Log.d("medicine",medicine.getName());
                    medicine.setEffect(effect);
                    mMedicines.add(medicine);
                }while (cursor.moveToNext());

            }


        return mMedicines;
    }
}

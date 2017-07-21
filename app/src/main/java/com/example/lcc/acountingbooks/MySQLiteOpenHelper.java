package com.example.lcc.acountingbooks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="DB";          //資料庫名稱
    private static final int DB_VERSION = 1;           //資料庫版本
    private static final String TABLE_NAME = "AccountNote"; //資料表名稱
    private static final String COL_id = "_id";
    private static final String COL_year = "year";
    private static final String COL_month = "month";
    private static final String COL_day = "day";
    private static final String COL_income = "income";
    private static final String COL_item = "item";
    private static final String COL_paymentMethods = "paymentMethods";
    private static final String COL_money = "money";
    private static final String COL_info = "info";

    //設計TABLE_NAME = "AccountNote"的資料表參數
    private static final String createTable = "CREATE TABLE IF NOT EXISTS"+" "
                                        + TABLE_NAME + " ( "
                                        + COL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + COL_year + " INTEGER, "
                                        + COL_month + " INTEGER, "
                                        + COL_day +" INTEGER, "
                                        + COL_income + " TEXT, "
                                        + COL_item + " TEXT, "
                                        + COL_paymentMethods + " TEXT,"
                                        + COL_money + " INTEGER NOT NULL, "
                                        + COL_info + " TEXT ); ";

    //設計TABLE_NAME = "CalNote"的資料表參數


    private static MySQLiteOpenHelper instance;
    public static  MySQLiteOpenHelper getInstance (Context context){
        if (instance == null){
            instance = new MySQLiteOpenHelper(context,DB_NAME,null,DB_VERSION);
        }
        return instance;
    }

    private MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context, name , factory , version);
    }

    public String select (MySQLiteOpenHelper helper,int setyear,int setmonth,int setday) {
        Cursor cursor = helper.getReadableDatabase().query("CalenderNote",new String[]{"noteinfo"},"year = ? AND month = ? AND day = ? ",new String[]{String.valueOf(setyear),String.valueOf(setmonth),String.valueOf(setday)},null,null,null);

        return (cursor.moveToNext()?cursor.getCount():0) != 0?cursor.getString(0):"目前尚無資料";//此為三元條件運算式
    }
    //自訂月曆查詢

    //建立資料表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
        db.execSQL("CREATE TABLE IF NOT EXISTS CalenderNote (  year INTEGER  , month INTEGER  , day  INTEGER  ,  noteinfo TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

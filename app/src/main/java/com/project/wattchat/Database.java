package com.project.wattchat;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    /*Bu sınıfta bir kullanıcı veritabanı oluşturulur.*/
    private static final String DATABASE="user.db";
    private static final int VERSION=1;
    public Database(Context con) {
        super(con, DATABASE, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE person (id INTEGER PRİMARY KEY AUTOINCREMENT, name TEXT, username TEXT, password TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE If EXIST person");
        onCreate(db);
    }
    /*bu metot veri tabanına kayıt işlemi gerçekleştirilmesini sağlar.*/
    public void addPerson(String name, String username, String password) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues datas=new ContentValues();
        datas.put("name", name);
        datas.put("username", username);
        datas.put("password", password);
        db.insertOrThrow("person", null, datas);
    }
    /* bu metod veritabanında kaıtlı kullanıcı sayısını döndürür.*/
    public int tableCount() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT count(*) FROM person", null);
        cursor.moveToNext();
        int result=cursor.getInt(0);
        cursor.close();
        return result;
    }
    /* bu metot kullanıcının ad ve şifre bilgilerini doğru grip girmediğini kontrol eder.*/
    public int IsPersonExist(String sername, String password) {
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT count(*) FROM person where username="+" username "+"and password="+"password"+"",null);
        cursor.moveToNext();
        int count=cursor.getInt(0);
        cursor.close();
        return count;
    }
    /*update metodu kullanıcının bilgilerini güncellemesi sağlar. yeni bilgiler veritabanına kaydedilir.*/
    public void update(String name, String username, String password) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name", name);
        cv.put("username", username);
        cv.put("password", password);
        db.update("person", cv, "name"+"=?", new String[] {name});
        db.close();
    }
    /*records() metodu veritabanından kayıtlı kullanıcı bilgilerini getirir. */
    public String[] records() {
        String name="", username="", password="";
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT*FROM person", null);
        while (cursor.moveToNext()) {
            name=cursor.getString((cursor.getColumnIndex("name")));
            username=cursor.getString((cursor.getColumnIndex("username")));
            password=cursor.getString((cursor.getColumnIndex("password")));
        }
        return new String[] {name, username, password};
    }
}

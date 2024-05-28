package com.atharvakale.facerecognition;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DB_Helpers extends SQLiteOpenHelper {

    // Veritabanı güncellemeleri
    public DB_Helpers(@Nullable Context context) {
        super(context, DB_constants.DB_NAME, null, DB_constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tablo oluşturma
        db.execSQL(DB_constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eski tabloyu silip güncellenmiş tabloyu yeniden oluştur.
        db.execSQL("DROP TABLE IF EXISTS " + DB_constants.DB_TABLE_NAME);
        onCreate(db);
    }

    public long add_record(byte[] imageByteArray, String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(date));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault());
        String newDate = dateFormat.format(calendar.getTime());

        values.put(DB_constants.C_IMAGE, imageByteArray);
        values.put(DB_constants.C_NAME, name);
        values.put(DB_constants.C_DATE, newDate);

        // Veri eklemesi işlemi
        long id = db.insert(DB_constants.DB_TABLE_NAME, null, values);

        // Veri eklemesi işlemi başarılı oldu mu kontrolü
        if (id != -1) {
            // Veri başarıyla eklendi
            Log.d("Veritabanı", "Veri başarıyla eklendi. ID: " + id);
        } else {
            // Veri eklenemedi, uygun şekilde işlem yapabilirsiniz
            Log.d("Veritabanı", "Veri eklenirken bir hata oluştu.");
        }

        // Veritabanı bağlantısını kapatma
        db.close();

        return id;
    }


    // Veri silme
    public void deleteData(String id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DB_constants.DB_TABLE_NAME, DB_constants.C_ID + " =? ", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        Log.d("Database", "Veri silindi: ID = " + id);
    }

    // Verileri getirme
    public ArrayList<records> fetchRecord(String sort) {
        ArrayList<records> rList = new ArrayList<>();

        String select = "SELECT * FROM " + DB_constants.DB_TABLE_NAME + " ORDER BY " + sort;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(select, null);

        // Cursor'un boş veya null olup olmadığını kontrol etme
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(DB_constants.C_IMAGE));
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                @SuppressLint("Range") records rec = new records(
                        "" + cursor.getInt(cursor.getColumnIndex(DB_constants.C_ID)),
                        image,
                        "" + cursor.getString(cursor.getColumnIndex(DB_constants.C_NAME)),
                        "" + cursor.getString(cursor.getColumnIndex(DB_constants.C_DATE))) ;

                rList.add(rec);

            } while (cursor.moveToNext());
        } else {
            // Cursor boş veya null ise uygun şekilde işlem yapabilirsiniz
            // Örneğin, bir log mesajı yazdırabilir veya bir hata mesajı gösterebilirsiniz
            Log.d("Veritabanı", "Veri bulunamadı veya Cursor null");
        }

        // Cursor'ı kapatma
        if (cursor != null) {
            cursor.close();
        }

        // Veritabanı bağlantısını kapatma
        sqLiteDatabase.close();

        return rList;
    }

    public static void copyDatabase(Context context) {
        try {
            // Veritabanı dosyasının yerini belirtin
            File dbFile = context.getDatabasePath("RECORDS.db"); // .db uzantısını eklediğinizden emin olun
            FileInputStream fis = new FileInputStream(dbFile);

            // Hedef dosya yolu
            File outFile = new File(context.getExternalFilesDir(null), "RECORDS.db"); // Doğru ve güvenli bir yol
            FileOutputStream fos = new FileOutputStream(outFile);

            // Dosyayı kopyalama işlemi
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            // Akışları kapatma
            fos.flush();
            fos.close();
            fis.close();

            Log.d("DatabaseHelper", "Veritabanı başarıyla kopyalandı: " + outFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("DatabaseHelper", "Veritabanı kopyalanırken hata oluştu", e);
        }
    }

}
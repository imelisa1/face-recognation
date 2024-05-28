package com.atharvakale.facerecognition;

public class DB_constants {
    // veri tabanı adı
    public static final String DB_NAME = "RECORDS";

    //veri tabanı versiyon
    public static final int DB_VERSION = 1;

    // veri tabanı tablo adı
    public static final String DB_TABLE_NAME = "RECORDS_TABLE";

    // Sütunlar
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_IMAGE = "IMAGE"; // Görüntüyü saklamak için BLOB tipini kullanın
    public static final String C_DATE = "DATE";
    public  static final String C_EMBEDDING = "EMBEDDING";

    // SQL tablo oluşturma
    public static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " BLOB, " // Byte dizisi olarak görüntüyü saklamak için BLOB tipi kullanın
            + C_NAME + " TEXT, "
            + C_DATE + " TEXT "
            + ")";
}


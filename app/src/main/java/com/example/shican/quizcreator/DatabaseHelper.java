package com.example.shican.quizcreator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 *this class is used to handle database operations
 * @author Nan Jiang
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PatientInfo";
    private static final int VERSION_NUM = 18;

    static final String TABLE_NAME = "patientTable";

    static final String KEY_ID = "id";
    static final String KEY_PATIENT_TYPE = "PatientType";
    static final String KEY_NAME = "Name";
    static final String KEY_ADDRESS = "Address";
    static final String KEY_AGE = "Age";
    static final String KEY_BIRTHDAY = "Birthday";
    static final String KEY_GENDER = "Gender";
    static final String KEY_PHONE_NUMBER = "PhoneNumber";
    static final String KEY_HEALTH_CARD_NUMBER = "HealthCardNumber";
    static final String KEY_DESCRIPTIOIN = "Description";
    static final String KEY_SURGERY = "Surgery";
    static final String KEY_ALLERGY = "Allergy";
    static final String KEY_BRACE = "Brace";
    static final String KEY_MEDICAL_BENEFIT = "MedicalBenefit";
    static final String KEY_GLASS_PURCHASE_DATE = "GlassPurchaseDate";
    static final String KEY_GLASS_PURCHASE_STORE = "GlassPurchaseStore";

    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    public void onCreate(SQLiteDatabase db) {
        String create_table_query = " CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_PATIENT_TYPE + " TEXT," +
                KEY_NAME + " Text," +
                KEY_ADDRESS + " TEXT," +
                KEY_AGE + " TEXT," +
                KEY_BIRTHDAY + " TEXT," +
                KEY_GENDER + " TEXT," +
                KEY_PHONE_NUMBER + " TEXT," +
                KEY_HEALTH_CARD_NUMBER + " TEXT," +
                KEY_DESCRIPTIOIN + " TEXT," +
                KEY_SURGERY + " TEXT," +
                KEY_ALLERGY + " TEXT," +
                KEY_BRACE + " TEXT," +
                KEY_MEDICAL_BENEFIT + " TEXT," +
                KEY_GLASS_PURCHASE_DATE + " TEXT," +
                KEY_GLASS_PURCHASE_STORE + " TEXT );";
        db.execSQL(create_table_query);
        Log.i("DatabaseHelper", "Calling onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("DatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion= " + newVer);
    }

}

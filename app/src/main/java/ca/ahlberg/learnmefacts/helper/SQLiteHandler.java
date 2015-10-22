package ca.ahlberg.learnmefacts.helper;

/**
 * Created by evan on 9/20/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LearnMeFacts";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String FACT_TABLE = "facts";
    private static final String FACT_ID = "id";
    private static final String FACT_CATEGORY = "category";
    private static final String FACT_SUBCATEGORY = "subcategory";
    private static final String FACT_FACT = "FACT";

    private static final String MYC_TABLE = "mycollection";
    private static final String MYC_FACTID = "factid";
    private static final String MYC_USERID = "userid";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_FACT_TABLE = "CREATE TABLE " + FACT_TABLE + "("
                + FACT_ID + " INTEGER PRIMARY KEY," + FACT_CATEGORY + " TEXT,"
                + FACT_SUBCATEGORY + " TEXT," + FACT_FACT + " TEXT" + ")";
        db.execSQL(CREATE_FACT_TABLE);

        String CREATE_MYC_TABLE = "CREATE TABLE " + MYC_TABLE + "("
                + MYC_FACTID + " INTEGER PRIMARY KEY," + MYC_USERID + " INTEGER PRIMARY KEY" + ")";
        db.execSQL(CREATE_MYC_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + FACT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MYC_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addFact(String category, String subcategory, String fact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FACT_CATEGORY, category);
        values.put(FACT_SUBCATEGORY, subcategory);
        values.put(FACT_FACT, fact);

        long id = db.insert(FACT_TABLE, null, values);

        Log.d(TAG, "New fact inserted: " + id);
    }

    public void addtoCollection(int factid, int userid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MYC_FACTID, factid);
        values.put(MYC_USERID, userid);

        long id = db.insert(MYC_TABLE, null, values);

        Log.d(TAG, "Fact inserted into Collection " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public HashMap<String, String> getFact() {
        HashMap<String, String> fact = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + FACT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            fact.put("category", cursor.getString(1));
            fact.put("subcategory", cursor.getString(2));
            fact.put("fact", cursor.getString(3));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Fetching fact from Sqlite: " + fact.toString());

        return fact;
    }

    /**
     * Recreate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteFact() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(FACT_TABLE, null, null);
        db.close();

        Log.d(TAG, "Deleted all facts from sqlite");
    }

}
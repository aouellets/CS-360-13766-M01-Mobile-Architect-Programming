package com.example.weighttrackingapp_alexouellet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final String TABLE_NAME = "users";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";
    private static final String WEIGHT_TABLE_NAME = "weights";
    private static final String WEIGHT_COL_1 = "ID";
    private static final String WEIGHT_COL_2 = "DATE";
    private static final String WEIGHT_COL_3 = "WEIGHT";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE " + WEIGHT_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, WEIGHT REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Modified method to add a user with a hashed password
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        
        // Hash the password before storing
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        contentValues.put(COL_3, hashedPassword);
        
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Modified method to check user with hashed password
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME = ?", new String[]{username});
        
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String storedHash = cursor.getString(cursor.getColumnIndex(COL_3));
            cursor.close();
            
            // Check if the provided password matches the stored hash
            return BCrypt.checkpw(password, storedHash);
        }
        
        cursor.close();
        return false;
    }

    public boolean addWeightEntry(String date, float weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIGHT_COL_2, date);
        contentValues.put(WEIGHT_COL_3, weight);
        long result = db.insert(WEIGHT_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllWeights() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + WEIGHT_TABLE_NAME + " ORDER BY " + WEIGHT_COL_2 + " DESC", null);
    }

    // Method to update a weight entry
    public boolean updateWeightEntry(int id, String date, float weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIGHT_COL_2, date);
        contentValues.put(WEIGHT_COL_3, weight);
        
        // Updating row
        int result = db.update(WEIGHT_TABLE_NAME, contentValues, WEIGHT_COL_1 + " = ?", new String[] { String.valueOf(id) });
        return result > 0;
    }

    // Method to delete a weight entry
    public boolean deleteWeightEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Deleting row
        int result = db.delete(WEIGHT_TABLE_NAME, WEIGHT_COL_1 + " = ?", new String[] { String.valueOf(id) });
        return result > 0;
    }

    // Method to get a single weight entry
    public WeightEntry getWeightEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(WEIGHT_TABLE_NAME, new String[] { WEIGHT_COL_1, WEIGHT_COL_2, WEIGHT_COL_3 },
                WEIGHT_COL_1 + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();

        WeightEntry entry = new WeightEntry(
            cursor.getInt(cursor.getColumnIndex(WEIGHT_COL_1)),
            cursor.getString(cursor.getColumnIndex(WEIGHT_COL_2)),
            cursor.getFloat(cursor.getColumnIndex(WEIGHT_COL_3))
        );
        
        cursor.close();
        return entry;
    }
}

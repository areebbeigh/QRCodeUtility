/**
 * Copyright 2016 Areeb Beigh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.areebbeigh.qrcodeutility.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class HistoryDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "qrcodeutility.db";
    private static final String TABLE_SCAN_HISTORY = "scan_history";
    private static final String TABLE_CREATE_HISTORY = "create_history";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_ERROR_CORRECTION_LEVEL = "ecl";
    private static final String COLUMN_FOREGROUND = "foreground";

    public HistoryDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE " + TABLE_SCAN_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_ERROR_CORRECTION_LEVEL + " TEXT" + ")" + ";";
        String query2 = "CREATE TABLE " + TABLE_CREATE_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_ERROR_CORRECTION_LEVEL + " TEXT, " +
                COLUMN_FOREGROUND + " TEXT" + ")" + ";";
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Nothing here yet
    }

    public void addScanItem(String text, String ecl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_ERROR_CORRECTION_LEVEL, ecl);
        db.insert(TABLE_SCAN_HISTORY, null, values);
        db.close();
    }

    public void deleteScanHistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SCAN_HISTORY, "1 == 1", null);
        db.close();
    }

    public void deleteCreateHistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CREATE_HISTORY, "1 == 1", null);
        db.close();
    }

    public void addCreateItem(String text, String ecl, int foregroundColor) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_ERROR_CORRECTION_LEVEL, ecl);
        values.put(COLUMN_FOREGROUND, foregroundColor);
        long i = db.insert(TABLE_CREATE_HISTORY, null, values);
        Log.i("com.areebbeigh", "Insert code: " + i);
        db.close();
    }

    /**
     * Returns an ArrayList<String[]> with String[2] arrays with the text at index 0
     * and error correction level at index 1
     */
    public ArrayList<String[]> scanDatabaseToList() {
        String query = "SELECT * FROM " + TABLE_SCAN_HISTORY;
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery(query, null);
        c.moveToLast(); // We need to get the most recent item first
        ArrayList<String[]> textList = new ArrayList<>();

        while (!c.isBeforeFirst()) {
            String text = c.getString(c.getColumnIndex(COLUMN_TEXT));
            String ecl = c.getString(c.getColumnIndex(COLUMN_ERROR_CORRECTION_LEVEL));
            textList.add(new String[]{text, ecl});
            c.moveToPrevious();
        }

        c.close();
        return textList;
    }

    public ArrayList<String[]> createDatabaseToList() {
        String query = "SELECT * FROM " + TABLE_CREATE_HISTORY;
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery(query, null);
        c.moveToLast(); // We need to get the most recent item first
        ArrayList<String[]> textList = new ArrayList<>();

        while (!c.isBeforeFirst()) {
            String text = c.getString(c.getColumnIndex(COLUMN_TEXT));
            String ecl = c.getString(c.getColumnIndex(COLUMN_ERROR_CORRECTION_LEVEL));
            String foreground = c.getString(c.getColumnIndex(COLUMN_FOREGROUND));
            textList.add(new String[]{text, ecl, foreground});
            c.moveToPrevious();
        }

        c.close();
        return textList;
    }
}

/*
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.hmdm.launcher.db;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hmdm.launcher.json.AppUsageEvent;

import java.util.LinkedList;
import java.util.List;

public class AppUsageTable {
    private static final String CREATE_TABLE =
            "CREATE TABLE app_usage (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "pkg TEXT, " +
                    "name TEXT, " +
                    "ts INTEGER, " +
                    "startedAt INTEGER, " +
                    "endedAt INTEGER, " +
                    "durationMs INTEGER " +
                    ")";
    private static final String INSERT_EVENT =
            "INSERT OR IGNORE INTO app_usage(pkg, name, ts, startedAt, endedAt, durationMs) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_EVENTS =
            "SELECT * FROM app_usage ORDER BY startedAt LIMIT ?";
    private static final String DELETE_EVENT =
            "DELETE FROM app_usage WHERE _id=?";
    private static final String DELETE_OLD_EVENTS =
            "DELETE FROM app_usage WHERE startedAt < ?";

    public static String getCreateTableSql() {
        return CREATE_TABLE;
    }

    public static void insert(SQLiteDatabase db, AppUsageEvent item) {
        if (item == null || item.getPkg() == null || item.getStartedAt() == null || item.getEndedAt() == null) {
            return;
        }
        try {
            db.execSQL(INSERT_EVENT, new String[]{
                    item.getPkg(),
                    item.getName(),
                    Long.toString(item.getTs() != null ? item.getTs() : item.getStartedAt()),
                    Long.toString(item.getStartedAt()),
                    Long.toString(item.getEndedAt()),
                    Long.toString(item.getDurationMs() != null ? item.getDurationMs() : item.getEndedAt() - item.getStartedAt())
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOldItems(SQLiteDatabase db) {
        long oldTs = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L;
        try {
            db.execSQL(DELETE_OLD_EVENTS, new String[]{
                    Long.toString(oldTs)
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(SQLiteDatabase db, List<AppUsageEvent> items) {
        db.beginTransaction();
        try {
            for (AppUsageEvent item : items) {
                db.execSQL(DELETE_EVENT, new String[]{
                        Long.toString(item.getId())
                });
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @SuppressLint("Range")
    public static List<AppUsageEvent> select(SQLiteDatabase db, int limit) {
        Cursor cursor = db.rawQuery(SELECT_EVENTS, new String[]{
                Integer.toString(limit)
        });
        List<AppUsageEvent> result = new LinkedList<>();

        boolean hasData = cursor.moveToFirst();
        while (hasData) {
            AppUsageEvent item = new AppUsageEvent();
            item.setId(cursor.getLong(cursor.getColumnIndex("_id")));
            item.setPkg(cursor.getString(cursor.getColumnIndex("pkg")));
            item.setName(cursor.getString(cursor.getColumnIndex("name")));
            item.setTs(cursor.getLong(cursor.getColumnIndex("ts")));
            item.setStartedAt(cursor.getLong(cursor.getColumnIndex("startedAt")));
            item.setEndedAt(cursor.getLong(cursor.getColumnIndex("endedAt")));
            item.setDurationMs(cursor.getLong(cursor.getColumnIndex("durationMs")));
            result.add(item);
            hasData = cursor.moveToNext();
        }
        cursor.close();

        return result;
    }
}

package org.techtown.feelim

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
    ) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL("CREATE TABLE movieList (mvTitle CHAR(20) PRIMARY KEY, mvStartDate CHAR, mvFinishDate CHAR, mvGenre CHAR, mvScore CHAR, mvPlace CHAR, myMemo CHAR);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS movieList")   // 존재할 때만 삭제 : IF EXISTS 추가
            onCreate(db)
        }
        }

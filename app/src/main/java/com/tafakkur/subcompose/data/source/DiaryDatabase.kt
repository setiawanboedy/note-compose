package com.tafakkur.subcompose.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.util.ImageConverters

@Database(entities = [Diary::class], version = 1)
@TypeConverters(ImageConverters::class)
abstract class DiaryDatabase: RoomDatabase() {
    abstract val diaryDao: DiaryDao

    companion object {
        const val DATABASE_NAME = "diary_db"
    }
}
package com.tafakkur.subcompose.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tafakkur.subcompose.domain.model.Diary
import kotlinx.coroutines.flow.Flow


@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary")
    fun getDiaries(): Flow<List<Diary>>

    @Query("SELECT * FROM diary WHERE id = :id")
    suspend fun getDiaryById(id: Int): Diary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDiary(diary: Diary)

    @Query("SELECT * FROM diary WHERE title LIKE :query")
    fun searchDiaries(query: String): Flow<List<Diary>>

    @Delete
    suspend fun deleteDiary(diary: Diary)
}
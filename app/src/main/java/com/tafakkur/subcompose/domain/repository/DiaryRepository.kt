package com.tafakkur.subcompose.domain.repository

import com.tafakkur.subcompose.domain.model.Diary
import kotlinx.coroutines.flow.Flow


interface DiaryRepository {
    fun getDiaries(): Flow<List<Diary>>

    suspend fun getDiaryById(id: Int): Diary?

    suspend fun addDiary(diary: Diary)

    suspend fun deleteDiary(diary: Diary)
}
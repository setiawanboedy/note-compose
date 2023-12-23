package com.tafakkur.subcompose.data.repository

import com.tafakkur.subcompose.data.source.DiaryDao
import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow

class DiaryRepositoryImpl(
    private val dao: DiaryDao
): DiaryRepository {
    override fun getDiaries(): Flow<List<Diary>> {
        return dao.getDiaries()
    }

    override suspend fun getDiaryById(id: Int): Diary? {
        return dao.getDiaryById(id)
    }

    override suspend fun addDiary(diary: Diary) {
        return dao.addDiary(diary)
    }

    override suspend fun deleteDiary(diary: Diary) {
        return dao.deleteDiary(diary)
    }

}
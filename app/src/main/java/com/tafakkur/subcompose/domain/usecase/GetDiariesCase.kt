package com.tafakkur.subcompose.domain.usecase

import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow

class GetDiariesCase(
    private val repository: DiaryRepository
) {
    operator fun invoke(): Flow<List<Diary>> {
        return repository.getDiaries()
    }
}
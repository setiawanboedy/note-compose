package com.tafakkur.subcompose.domain.usecase

import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.repository.DiaryRepository

class GetByIdCase(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(id: Int): Diary? {
        return repository.getDiaryById(id)
    }
}
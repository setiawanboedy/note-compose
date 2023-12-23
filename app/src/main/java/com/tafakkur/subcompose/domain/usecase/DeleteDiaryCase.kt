package com.tafakkur.subcompose.domain.usecase

import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.repository.DiaryRepository

class DeleteDiaryCase(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(diary: Diary){
        repository.deleteDiary(diary)
    }
}
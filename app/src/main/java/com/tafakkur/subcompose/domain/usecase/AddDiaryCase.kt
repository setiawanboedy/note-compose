package com.tafakkur.subcompose.domain.usecase

import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.repository.DiaryRepository
import com.tafakkur.subcompose.domain.util.InvalidDiaryException

class AddDiaryCase(
    private val repository: DiaryRepository
) {
    @Throws(InvalidDiaryException::class)
    suspend operator fun invoke(diary: Diary){
        if (diary.title.isBlank()){
            throw InvalidDiaryException("Title cannot empty")
        }
        if (diary.description.isBlank()){
            throw InvalidDiaryException("Description cannot empty")
        }
        if (diary.image == null){
            throw InvalidDiaryException("Image must be added")
        }
        repository.addDiary(diary)
    }
}
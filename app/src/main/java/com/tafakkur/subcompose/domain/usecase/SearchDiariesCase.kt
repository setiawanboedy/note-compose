package com.tafakkur.subcompose.domain.usecase

import com.tafakkur.subcompose.domain.model.Diary
import com.tafakkur.subcompose.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow

class SearchDiariesCase(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Diary>> {
        return repository.searchDiaries(query)
    }
}
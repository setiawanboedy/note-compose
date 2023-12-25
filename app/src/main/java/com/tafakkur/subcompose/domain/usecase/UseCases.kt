package com.tafakkur.subcompose.domain.usecase

data class UseCases(
    val addDiary: AddDiaryCase,
    val getDiaries: GetDiariesCase,
    val deleteDiaryCase: DeleteDiaryCase,
    val getByIdCase: GetByIdCase,
    val searchDiariesCase: SearchDiariesCase
)

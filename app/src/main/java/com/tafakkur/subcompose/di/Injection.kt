package com.tafakkur.subcompose.di

import android.app.Application
import androidx.room.Room
import com.tafakkur.subcompose.data.repository.DiaryRepositoryImpl
import com.tafakkur.subcompose.data.source.DiaryDatabase
import com.tafakkur.subcompose.domain.repository.DiaryRepository
import com.tafakkur.subcompose.domain.usecase.AddDiaryCase
import com.tafakkur.subcompose.domain.usecase.DeleteDiaryCase
import com.tafakkur.subcompose.domain.usecase.GetByIdCase
import com.tafakkur.subcompose.domain.usecase.GetDiariesCase
import com.tafakkur.subcompose.domain.usecase.SearchDiariesCase
import com.tafakkur.subcompose.domain.usecase.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Injection {
    @Provides
    @Singleton
    fun provideDiaryDatabase(application: Application): DiaryDatabase{
        return Room.databaseBuilder(
            application,
            DiaryDatabase::class.java,
            DiaryDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDiaryRepository(database: DiaryDatabase): DiaryRepository
    = DiaryRepositoryImpl(database.diaryDao)

    @Provides
    @Singleton
    fun provideUseCases(repository: DiaryRepository): UseCases {
        return UseCases(
            getByIdCase = GetByIdCase(repository),
            deleteDiaryCase = DeleteDiaryCase(repository),
            addDiary = AddDiaryCase(repository),
            getDiaries = GetDiariesCase(repository),
            searchDiariesCase = SearchDiariesCase(repository)
        )
    }
    @Provides
    @Singleton
    fun provideAddDiaryCase(repository: DiaryRepository): AddDiaryCase {
        return AddDiaryCase(repository)
    }
}
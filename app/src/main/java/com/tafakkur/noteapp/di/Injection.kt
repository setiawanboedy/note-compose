package com.tafakkur.noteapp.di

import android.app.Application
import androidx.room.Room
import com.tafakkur.noteapp.data.repository.NoteRepositoryImpl
import com.tafakkur.noteapp.data.source.NoteDatabase
import com.tafakkur.noteapp.domain.repository.NoteRepository
import com.tafakkur.noteapp.domain.usecase.AddNoteCase
import com.tafakkur.noteapp.domain.usecase.DeleteNoteCase
import com.tafakkur.noteapp.domain.usecase.GetByIdCase
import com.tafakkur.noteapp.domain.usecase.GetDiariesCase
import com.tafakkur.noteapp.domain.usecase.SearchDiariesCase
import com.tafakkur.noteapp.domain.usecase.UseCases
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
    fun provideNoteDatabase(application: Application): NoteDatabase{
        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
            .createFromAsset("note_db.sql")
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(database: NoteDatabase): NoteRepository
    = NoteRepositoryImpl(database.noteDao)

    @Provides
    @Singleton
    fun provideUseCases(repository: NoteRepository): UseCases {
        return UseCases(
            getByIdCase = GetByIdCase(repository),
            deleteNoteCase = DeleteNoteCase(repository),
            addNote = AddNoteCase(repository),
            getDiaries = GetDiariesCase(repository),
            searchDiariesCase = SearchDiariesCase(repository)
        )
    }
    @Provides
    @Singleton
    fun provideAddNoteCase(repository: NoteRepository): AddNoteCase {
        return AddNoteCase(repository)
    }
}
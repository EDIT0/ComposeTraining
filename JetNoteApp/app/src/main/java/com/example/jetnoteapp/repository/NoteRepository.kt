package com.example.jetnoteapp.repository

import com.example.jetnoteapp.data.NoteDatabaseDao
import com.example.jetnoteapp.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDatabaseDao: NoteDatabaseDao
) {

    suspend fun addNote(note: Note) {
        return noteDatabaseDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        return noteDatabaseDao.update(note)
    }

    suspend fun deleteNote(note: Note) {
        return noteDatabaseDao.deleteNote(note)
    }

    suspend fun deleteAllNotes() {
        return noteDatabaseDao.deleteAll()
    }

    fun getAllNotes(): Flow<List<Note>> {
        return noteDatabaseDao.getNotes()
            .flowOn(Dispatchers.IO)
            .conflate()
    }
}
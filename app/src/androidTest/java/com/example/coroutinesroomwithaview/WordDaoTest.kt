package com.example.coroutinesroomwithaview

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WordDaoTest {

    private lateinit var wordDao: WordDao
    private lateinit var db: WordRoomDatabase


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase::class.java)
            .allowMainThreadQueries()//dont do this in code, acceptable in testing if you know what you are doing
            .build()
        wordDao = db.wordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val word = Word("testword")
        wordDao.insert(word)
        val allWords = wordDao.getAlphabetizedWords().first()
        assertEquals(allWords[0].word, word.word)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDeleteWordAndGetEmptyResult() = runBlocking {
        val word = Word("testword")
        wordDao.insert(word)
        wordDao.deleteAll()
        val allWords = wordDao.getAlphabetizedWords().first()
        assertTrue(allWords.isEmpty())
    }

}

package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for TeacherEntity
 * Defines the data access layer for the Teacher Entity
 */
@Dao
interface TeacherDao {
    /** Returns all teachers, ordered by last name and first name. */
    @Query("SELECT * FROM teachers ORDER BY lastName, firstName")
    fun getAllTeachers(): Flow<List<TeacherEntity>>

    /** Inserts or updates a teacher. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teacher: TeacherEntity)

    /** Deletes a teacher. */
    @Delete
    suspend fun delete(teacher: TeacherEntity)

    /** Updates a teacher. */
    @Update
    suspend fun update(teacher: TeacherEntity)

    /** Retrieves a teacher by their ID. */
    @Query("SELECT * FROM teachers WHERE teacherId = :id LIMIT 1")
    suspend fun getTeacherById(id: Int): TeacherEntity?

    /** Authenticates a teacher by email and password. */
    @Query("SELECT * FROM teachers WHERE email = :email AND password = :password LIMIT 1")
    suspend fun authenticate(email: String, password: String): TeacherEntity?
}

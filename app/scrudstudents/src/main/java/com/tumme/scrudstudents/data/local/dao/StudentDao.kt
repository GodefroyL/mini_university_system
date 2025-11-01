package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

/** Data Access Object
 * Defines the Data access layer for the Student Entity
 * It is used by the repository to interact with the database
 * It is annotated with @Dao to tell Room that it is a Data Access Object
 * */
@Dao
interface StudentDao {

    /** Returns all students, ordered by last name and first name. */
    @Query("SELECT * FROM students ORDER BY lastName, firstName")
    fun getAllStudents(): Flow<List<StudentEntity>>

    /** Inserts or updates a student. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity)

    /** Deletes a student. */
    @Delete
    suspend fun delete(student: StudentEntity)

    /** Updates a student. */
    @Update
    suspend fun update(student: StudentEntity)

    /** Retrieves a student by their ID. */
    @Query("SELECT * FROM students WHERE idStudent = :id LIMIT 1")
    suspend fun getStudentById(id: Int): StudentEntity?

    /** Authenticates a student by email and password. */
    @Query("SELECT * FROM students WHERE email = :email AND password = :password LIMIT 1")
    suspend fun authenticate(email: String, password: String): StudentEntity?
}

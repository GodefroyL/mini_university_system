package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.StudentWithScore
import com.tumme.scrudstudents.data.local.model.SubscribeWithCourse
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for SubscribeEntity
 * Defines the data access layer for the Subscribe Entity
 */
@Dao
interface SubscribeDao {
    /** Returns all subscriptions. */
    @Query("SELECT * FROM subscribes")
    fun getAllSubscribes(): Flow<List<SubscribeEntity>>

    /** Inserts or updates a subscription. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscribe: SubscribeEntity)

    /** Deletes a subscription. */
    @Delete
    suspend fun delete(subscribe: SubscribeEntity)

    /** Updates a subscription. */
    @Update
    suspend fun update(subscribe: SubscribeEntity)

    /** Retrieves subscriptions by student ID. */
    @Query("SELECT * FROM subscribes WHERE studentId = :studentId")
    fun getSubscriptionsByStudent(studentId: Int): Flow<List<SubscribeEntity>>

    /** Retrieves subscriptions by course ID. */
    @Query("SELECT * FROM subscribes WHERE courseId = :courseId")
    fun getSubscriptionsByCourse(courseId: Int): Flow<List<SubscribeEntity>>

    /** Retrieves a subscription by student ID and course ID. */
    @Query("SELECT * FROM subscribes WHERE studentId = :studentId AND courseId = :courseId LIMIT 1")
    suspend fun getSubscription(studentId: Int, courseId: Int): SubscribeEntity?

    /** Updates the score for a subscription. */
    @Query("UPDATE subscribes SET score = :score WHERE studentId = :studentId AND courseId = :courseId")
    suspend fun updateScore(studentId: Int, courseId: Int, score: Float)

    /** Returns subscriptions with course details for a student. */
    @Transaction
    @Query("SELECT * FROM subscribes WHERE studentId = :studentId")
    fun getSubscriptionsWithCourses(studentId: Int): Flow<List<SubscribeWithCourse>>

    /** Returns students with scores for a course. */
    @Transaction
    @Query("""
        SELECT students.idStudent, students.firstName, students.lastName, subscribes.score
        FROM students
        JOIN subscribes ON students.idStudent = subscribes.studentId
        WHERE subscribes.courseId = :courseId
    """
    )
    fun getStudentsWithScoresByCourse(courseId: Int): Flow<List<StudentWithScore>>
}

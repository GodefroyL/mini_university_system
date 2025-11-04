package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentWithScore
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.SubscribeWithCourse
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscribeDao {
    @Query("SELECT * FROM subscribes")
    fun getAllSubscribes(): Flow<List<SubscribeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscribe: SubscribeEntity)

    @Delete
    suspend fun delete(subscribe: SubscribeEntity)

    @Update
    suspend fun update(subscribe: SubscribeEntity)

    @Query("SELECT * FROM subscribes WHERE studentId = :studentId")
    fun getSubscriptionsByStudent(studentId: Int): Flow<List<SubscribeEntity>>

    @Query("SELECT * FROM subscribes WHERE courseId = :courseId")
    fun getSubscriptionsByCourse(courseId: Int): Flow<List<SubscribeEntity>>

    @Query("SELECT * FROM subscribes WHERE studentId = :studentId AND courseId = :courseId LIMIT 1")
    suspend fun getSubscription(studentId: Int, courseId: Int): SubscribeEntity?

    @Query("UPDATE subscribes SET score = :score WHERE studentId = :studentId AND courseId = :courseId")
    suspend fun updateScore(studentId: Int, courseId: Int, score: Float)

    @Transaction
    @Query("SELECT * FROM subscribes WHERE studentId = :studentId")
    fun getSubscriptionsWithCourses(studentId: Int): Flow<List<SubscribeWithCourse>>

    @Transaction
    @Query("""
        SELECT s.idStudent, s.firstName, s.lastName, sub.score
        FROM students s
        JOIN subscribes sub ON s.idStudent = sub.studentId
        WHERE sub.courseId = :courseId
    """)
    fun getStudentsWithScoresByCourse(courseId: Int): Flow<List<StudentWithScore>>
}

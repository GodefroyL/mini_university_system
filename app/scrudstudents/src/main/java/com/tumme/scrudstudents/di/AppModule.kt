package com.tumme.scrudstudents.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tumme.scrudstudents.data.local.AppDatabase
import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        studentDaoProvider: Provider<StudentDao>,
        teacherDaoProvider: Provider<TeacherDao>,
        courseDaoProvider: Provider<CourseDao> // Add CourseDao provider
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "scrud-db")
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Pre-populate database on creation
                    CoroutineScope(Dispatchers.IO).launch {
                        // Teachers
                        teacherDaoProvider.get().insert(TeacherEntity(1, "Michel", "Vincent", "m.vincent@univ.com", "password"))
                        teacherDaoProvider.get().insert(TeacherEntity(2, "Remi", "Pasquier", "remi.pasquier@univ.com", "password"))

                        // Students
                        studentDaoProvider.get().insert(StudentEntity(1, "Louis", "Dupont", "louis.dupont@example.com", "password", "04/11/2025", Gender.Male, "B1"))
                        studentDaoProvider.get().insert(StudentEntity(2, "Jehanne", "de Boisgarnier", "j.dbg@example.com", "password", "04/11/2025", Gender.Female, "B2"))

                        // Courses (with levels that will now appear in registration)
                        courseDaoProvider.get().insert(CourseEntity(nameCourse = "Algorithmie", ectsCourse = 5.0f, levelCode = "B1", teacherId = 1, description = "Bases de l'algorithmie"))
                        courseDaoProvider.get().insert(CourseEntity(nameCourse = "Base de données", ectsCourse = 4.0f, levelCode = "B2", teacherId = 2, description = "Introduction à SQL"))
                        courseDaoProvider.get().insert(CourseEntity(nameCourse = "Développement Web", ectsCourse = 6.0f, levelCode = "B3", teacherId = 1, description = "HTML, CSS, JavaScript"))
                    }
                }
            })
            .build()
    }

    /**
     * Provides the DAOs for all entities
     */
    @Provides
    fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()

    @Provides
    fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()

    @Provides
    fun provideSubscribeDao(db: AppDatabase): SubscribeDao = db.subscribeDao()

    @Provides
    fun provideTeacherDao(db: AppDatabase): TeacherDao = db.teacherDao()

    @Provides
    @Singleton
    fun provideRepository(
        studentDao: StudentDao,
        teacherDao: TeacherDao,
        courseDao: CourseDao,
        subscribeDao: SubscribeDao
    ): SCRUDRepository = SCRUDRepository(studentDao, teacherDao, courseDao, subscribeDao)
}

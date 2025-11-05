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
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "scrud-db-final-final-version") // Final name change
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.beginTransaction()
                    try {
                        // Pre-populate teachers
                        db.execSQL("INSERT INTO teachers (firstName, lastName, email, password, gender, registrationDate) VALUES ('Michel', 'Vincent', 'm.vincent@univ.com', 'password', 'Male', 1672531200000)")
                        db.execSQL("INSERT INTO teachers (firstName, lastName, email, password, gender, registrationDate) VALUES ('Remi', 'Pasquier', 'remi.pasquier@univ.com', 'password', 'Male', 1672531200000)")
                        // Pre-populate students
                        db.execSQL("INSERT INTO students (firstName, lastName, email, password, dateOfBirth, gender, levelCode, registrationData) VALUES ('Louis', 'Dupont', 'louis.dupont@example.com', 'password', '2001-11-09', 'Male', 'B1', 1672531200000)")
                        db.execSQL("INSERT INTO students (firstName, lastName, email, password, dateOfBirth, gender, levelCode, registrationData) VALUES ('Jehanne', 'de Boisgarnier', 'j.dbg@example.com', 'password', '2005-03-04', 'Female', 'B2', 1672531200000)")
                        // Pre-populate courses and assign them to teachers (assuming teacher IDs are 1 and 2)
                        db.execSQL("INSERT INTO courses (nameCourse, ectsCourse, levelCode, description, teacherId) VALUES ('Algorithmie', 5.0, 'B1', 'Bases de l\'\'algorithmie', 1)")
                        db.execSQL("INSERT INTO courses (nameCourse, ectsCourse, levelCode, description, teacherId) VALUES ('Base de données', 4.0, 'B2', 'Introduction à SQL', 2)")
                        db.execSQL("INSERT INTO courses (nameCourse, ectsCourse, levelCode, description, teacherId) VALUES ('Développement Web', 6.0, 'B3', 'HTML, CSS, JavaScript', 1)")
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }
            })
            .build()
    }

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

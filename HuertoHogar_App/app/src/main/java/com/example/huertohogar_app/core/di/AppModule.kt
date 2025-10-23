package com.example.huertohogar_app.di

import android.content.Context
import com.example.huertohogar_app.data.local.AdminSQLiteOpenHelper
import com.example.huertohogar_app.data.repository.AuthRepositoryImpl
import com.example.huertohogar_app.domain.use_case.repository.AuthRepository
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
    fun provideDatabase(@ApplicationContext context: Context): AdminSQLiteOpenHelper {
        return AdminSQLiteOpenHelper(
            context = context,
            name = "huerto_hogar_db",
            factory = null,
            version = 1
        )
    }

    @Provides
    @Singleton
    fun provideAdminSQLiteOpenHelper(@ApplicationContext context: Context): AdminSQLiteOpenHelper {
        return AdminSQLiteOpenHelper(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(dbHelper: AdminSQLiteOpenHelper): AuthRepository {
        return AuthRepositoryImpl(dbHelper)
    }
}
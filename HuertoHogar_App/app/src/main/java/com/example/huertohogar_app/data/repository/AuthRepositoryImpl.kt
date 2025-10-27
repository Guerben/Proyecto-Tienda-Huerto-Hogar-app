package com.example.huertohogar_app.data.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import com.example.huertohogar_app.data.local.AdminSQLiteOpenHelper
import com.example.huertohogar_app.domain.model.LoggedUser
import com.example.huertohogar_app.domain.model.RegisterUser
import com.example.huertohogar_app.domain.use_case.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val dbHelper: AdminSQLiteOpenHelper) : AuthRepository {

    override suspend fun register(user: RegisterUser): Result<Unit> = withContext(Dispatchers.IO) {

        try {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("name", user.name)
                put("email", user.email)
                put("password", user.pass)
            }
            db.insertOrThrow("usuarios", null, values)
            db.close()
            Result.success(Unit)
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, pass: String): Result<LoggedUser> = withContex(Dispatchers.IO) {
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT id, name, email FROM usuarios WHERE email = ? AND password = ?", arrayOf(email, pass))

            if (cursor.moveToFirst()) {
                val loggedUser = LoggedUser(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    email = cursor.getString(2)
                )
                cursor.close()
                db.close()
                Result.success(loggedUser)
            } else {
                cursor.close()
                db.close()
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}
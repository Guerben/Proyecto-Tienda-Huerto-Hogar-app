package com.example.huertohogar_app.core

class UiState {
    sealed class Resource<T>(val data: T? = null, val message: String? = null) {

        /** Indica que la operación fue exitosa y contiene los datos. */
        class Success<T>(data: T) : Resource<T>(data)

        /** Indica que la operación está en curso. */
        class Loading<T>(data: T? = null) : Resource<T>(data)

        /** Indica que ocurrió un error y contiene un mensaje. */
        class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    }

}
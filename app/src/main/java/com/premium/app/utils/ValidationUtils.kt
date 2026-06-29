package com.premium.app.utils

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // Mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}".toRegex()
        return password.matches(passwordRegex)
    }

    fun isValidUsername(username: String): Boolean {
        // Mínimo 3 caracteres, solo letras, números y guiones bajos
        return username.length >= 3 && username.matches("^[a-zA-Z0-9_]*$".toRegex())
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }

    fun isAgeValid(birthdate: String): Boolean {
        // Simple validation for now, assumes YYYY-MM-DD format
        // TODO: Implement more robust age validation
        return birthdate.matches("\\d{4}-\\d{2}-\\d{2}".toRegex())
    }
}

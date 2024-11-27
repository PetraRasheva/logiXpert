package com.example.logiXpert.constraint.validator;

import com.example.logiXpert.constraint.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password cannot be null or empty").addConstraintViolation();
            return false;
        }

        StringBuilder errorMessage = new StringBuilder();

        // Проверка за дължина
        if (password.length() < 8) {
            errorMessage.append("at least 8 characters, ");
        }

        // Проверка за главна буква
        if (!password.matches(".*[A-Z].*")) {
            errorMessage.append("uppercase letter, ");
        }

        // Проверка за цифра
        if (!password.matches(".*\\d.*")) {
            errorMessage.append("digit, ");
        }

        // Проверка за специален символ
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            errorMessage.append("special character, ");
        }

        // Ако има нарушения, върни обобщено съобщение
        if (errorMessage.length() > 0) {
            // Премахни последната запетая и интервал
            errorMessage.setLength(errorMessage.length() - 2);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Password must contain " + errorMessage.toString()
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
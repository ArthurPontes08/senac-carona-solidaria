package com.senac.carona.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

final class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    static String hash(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] hash = derive(password, salt, ITERATIONS);
        return "pbkdf2$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    static boolean matches(String password, String encoded) {
        if (password == null || encoded == null) {
            return false;
        }
        String[] parts = encoded.split("\\$", -1);
        if (parts.length != 4 || !"pbkdf2".equals(parts[0])) {
            throw new IllegalStateException("Formato de senha armazenada inválido.");
        }
        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            return MessageDigest.isEqual(expected, derive(password, salt, iterations));
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Formato de senha armazenada inválido.", exception);
        }
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, HASH_LENGTH * 8);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Não foi possível processar a senha.", exception);
        } finally {
            spec.clearPassword();
        }
    }
}

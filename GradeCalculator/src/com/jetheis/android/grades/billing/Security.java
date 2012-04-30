package com.jetheis.android.grades.billing;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;

import com.jetheis.android.grades.Constants;
import com.jetheis.android.grades.R;

public class Security {

    /**
     * A 36 character long randomly generated String to be used as part of the
     * value stored when the full version of the app is unlocked. This value
     * should be changed before compiling an official version and not shared.
     * This value can easily be created on a UNIX-like platform with the
     * following command:
     * 
     * <pre>
     * $ head /dev/urandom | uuencode -m - | sed -n 2p | cut -c1-${1:-36}
     * </pre>
     */
    private static final String KEY_PART = "hdpWrCnAHxwl77YRQ539NBbBCBx7mqPvDsLA";

    /**
     * A 36 character long randomly generated String to be used as part of the
     * encryption process when the full version of the app is unlocked. This
     * value should be changed before compiling an official version and not
     * shared. This value can easily be created on a UNIX-like platform with the
     * following command:
     * 
     * <pre>
     * $ head /dev/urandom | uuencode -m - | sed -n 2p | cut -c1-${1:-36}
     * </pre>
     */
    private static final String PASSWORD_PART = "MiNwYzj3qAjLXtUhRCs1wZbhDG5eTY957Vbo";

    /**
     * A randomly generated array of bytes to be used as the initialization
     * vector of the AES cipher used to encrypt a token when the full version of
     * the app is unlocked. This value should be changed before compiling an
     * official version and not shared. This value can easily be created on a
     * UNIX-lik platform (with Python installed) with the following command:
     * 
     * <pre>
     * python -c "print str([__import__('random').randrange(-127, 128) for i in range(16)]).replace('[', '{').replace(']', '}')"
     * </pre>
     */
    private static final byte AES_IV[] = new byte[] { -39, 106, -80, -111, -78, -45, -103, 99, 93,
            -65, 124, -8, -47, 25, 61, 21 };

    private static final String KEYGEN_ALGORITHM = "PBEWITHSHAAND256BITAES-CBC-BC";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String UNLOCK_KEY_STORAGE_KEY = "unlock_key";

    private static final SecureRandom sRandom = new SecureRandom();

    public static void setFullVersionUnlocked(boolean fullVersionUnlocked, Context context) {
        if (fullVersionUnlocked) {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString(UNLOCK_KEY_STORAGE_KEY, createNewEncryptedUnlockKey(context));
            editor.commit();
        } else {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.remove(UNLOCK_KEY_STORAGE_KEY);
            editor.commit();
        }
    }

    public static boolean isFullVersionUnlocked(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String encrypted = prefs.getString(UNLOCK_KEY_STORAGE_KEY, null);

        if (encrypted == null) {
            return false;
        }

        if (getUnlockKeyForDevice(context).equals(decryptUnlockKey(encrypted, context))) {
            return true;
        }

        Log.w(Constants.TAG, "Old or incorrect key found. Deleting.");

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(UNLOCK_KEY_STORAGE_KEY);
        editor.commit();

        return false;
    }

    private static String createNewEncryptedUnlockKey(Context context) {
        byte salt[] = new byte[6];
        sRandom.nextBytes(salt);
        Log.v(Constants.TAG, "Salt chosen: " + Base64.encodeToString(salt, Base64.NO_WRAP));

        String result = encryptUnlockKey(context, salt);

        Log.v(Constants.TAG, "New encrypted data (length " + result.length() + "): " + result);

        return result;
    }

    private static String getUnlockKeyForDevice(Context context) {

        String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        String packageName = context.getPackageName();

        String versionName;

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Encoding error", e);
        }

        // Generate an "unlock key" based on the version name, device, the
        // package name, a resource file included with the app, and a static
        // variable in this class.
        String keyResourceString = context.getString(R.string.key_part);
        return versionName + deviceId + packageName + keyResourceString + KEY_PART;
    }

    private static char[] getPasswordForDevice(Context context) {
        String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        String packageName = context.getPackageName();

        String versionName;

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Encoding error", e);
        }

        // Generate a password based on the version name, device, the package
        // name, a resource file included with the app, and a static variable in
        // this class.
        String passwordResourceString = context.getString(R.string.password_part);
        return (versionName + deviceId + packageName + passwordResourceString + PASSWORD_PART)
                .toCharArray();
    }

    private static String encryptUnlockKey(Context context, byte[] salt) {
        Cipher encrypter;

        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEYGEN_ALGORITHM);
            KeySpec keySpec = new PBEKeySpec(getPasswordForDevice(context), salt, 1024, 256);

            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            encrypter = Cipher.getInstance(CIPHER_ALGORITHM);
            encrypter.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(AES_IV));

        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Invalid environment", e);
        }

        try {
            return Base64.encodeToString(salt, Base64.NO_WRAP)
                    + Base64.encodeToString(
                            encrypter.doFinal(getUnlockKeyForDevice(context).getBytes("UTF-8")),
                            Base64.NO_WRAP);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("Encoding error", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("Encoding error", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Invalid environment", e);
        }
    }

    private static String decryptUnlockKey(String encrypted, Context context) {
        if (encrypted == null) {
            return null;
        }

        byte[] salt = Base64.decode(encrypted.substring(0, 8), Base64.NO_WRAP);

        Log.v(Constants.TAG, "Found salt: " + encrypted.substring(0, 8));

        encrypted = encrypted.substring(8);

        Log.v(Constants.TAG, "Remaining encrypted data: " + encrypted);
        Log.v(Constants.TAG, "Length after salt removed: " + encrypted.length());

        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(KEYGEN_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        }

        KeySpec keySpec = new PBEKeySpec(getPasswordForDevice(context), salt, 1024, 256);

        SecretKey tmp;

        try {
            tmp = factory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        }

        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher decrypter;

        try {
            decrypter = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        } catch (NoSuchPaddingException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        }

        try {
            decrypter.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(AES_IV));
        } catch (InvalidKeyException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        }

        try {
            return new String(decrypter.doFinal(Base64.decode(encrypted, Base64.NO_WRAP)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        } catch (IllegalBlockSizeException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        } catch (BadPaddingException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        } catch (IllegalArgumentException e) {
            Log.e("Error decrypting: ", e.getLocalizedMessage());
            return null;
        }
    }
}

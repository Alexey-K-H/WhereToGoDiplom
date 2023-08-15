package ru.nsu.fit.wheretogo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class ObscuredSharedPreferences implements SharedPreferences {
    protected static final String UTF8 = "UTF-8";
    private static char[] SECRET = null;
    private static byte[] SALT = null;

    private static char[] backup_secret = null;
    private static byte[] backup_salt = null;

    protected SharedPreferences delegate;
    protected Context context;
    private static final java.util.HashMap<String, ObscuredSharedPreferences> prefs = new java.util.HashMap<String, ObscuredSharedPreferences>();

    public static boolean decryptionErrorFlag = false;

    @SuppressLint("HardwareIds")
    public ObscuredSharedPreferences(Context context, SharedPreferences delegate) {
        this.delegate = delegate;
        this.context = context;
        ObscuredSharedPreferences.setNewKey(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        ObscuredSharedPreferences.setNewSalt(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public static void setNewKey(String key) {
        SECRET = key.toCharArray();
    }

    public static void setNewSalt(String salt) {
        SALT = salt.getBytes(StandardCharsets.UTF_8);
    }

    public synchronized static ObscuredSharedPreferences getPrefs(Context c, String domain, int contextMode) {
        if (!prefs.containsKey(domain) || prefs.get(domain) == null) {
            //make sure to use application context since preferences live outside an Activity
            //use for objects that have global scope like: prefs or starting services
            prefs.put(domain, new ObscuredSharedPreferences(
                    c.getApplicationContext(), c.getApplicationContext().getSharedPreferences(domain, contextMode))
            );
        }
        return prefs.get(domain);
    }

    public class Editor implements SharedPreferences.Editor {
        protected SharedPreferences.Editor delegate;

        public Editor() {
            this.delegate = ObscuredSharedPreferences.this.delegate.edit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            delegate.putString(key, encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            delegate.putString(key, encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            delegate.putString(key, encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            delegate.putString(key, encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            delegate.putString(key, encrypt(value));
            return this;
        }

        @Override
        public void apply() {
            //to maintain compatibility with android level 7
            delegate.commit();
        }

        @Override
        public Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public Editor remove(String s) {
            delegate.remove(s);
            return this;
        }

        @Override
        public android.content.SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            throw new RuntimeException("This class does not work with String Sets.");
        }
    }

    public Editor edit() {
        return new Editor();
    }


    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException(); // left as an exercise to the reader
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        //if these weren't encrypted, then it won't be a string
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getBoolean(key, defValue);
        }
        //Boolean string values should be 'true' or 'false'
        //Boolean.parseBoolean does not throw a format exception, so check manually
        String parsed = decrypt(v);
        if (!checkBooleanString(parsed)) {
            //could not decrypt the Boolean.  Maybe the wrong key was used.
            decryptionErrorFlag = true;
            Log.e(this.getClass().getName(), "Warning, could not decrypt the value.  Possible incorrect key used.");
        }
        return v != null ? Boolean.parseBoolean(parsed) : defValue;
    }

    private boolean checkBooleanString(String str) {
        return ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str));
    }

    @Override
    public float getFloat(String key, float defValue) {
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getFloat(key, defValue);
        }
        try {
            return Float.parseFloat(decrypt(v));
        } catch (NumberFormatException e) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true;
            Log.e(this.getClass().getName(), "Warning, could not decrypt the value.  Possible incorrect key.  " + e.getMessage());
        }
        return defValue;
    }

    @Override
    public int getInt(String key, int defValue) {
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getInt(key, defValue);
        }
        try {
            return Integer.parseInt(decrypt(v));
        } catch (NumberFormatException e) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true;
            Log.e(this.getClass().getName(), "Warning, could not decrypt the value.  Possible incorrect key.  " + e.getMessage());
        }
        return defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getLong(key, defValue);
        }
        try {
            return Long.parseLong(decrypt(v));
        } catch (NumberFormatException e) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true;
            Log.e(this.getClass().getName(), "Warning, could not decrypt the value.  Possible incorrect key.  " + e.getMessage());
        }
        return defValue;
    }

    @Override
    public String getString(String key, String defValue) {
        final String v = delegate.getString(key, null);
        return v != null ? decrypt(v) : defValue;
    }

    @Override
    public boolean contains(String s) {
        return delegate.contains(s);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        throw new RuntimeException("This class does not work with String Sets.");
    }

    /**
     * Push key allows you to hold the current key being used into a holding location so that it can be retrieved later
     * The use case is for when you need to load a new key, but still want to restore the old one.
     */
    public static void pushKey() {
        backup_secret = SECRET;
    }

    /**
     * This takes the key previously saved by pushKey() and activates it as the current decryption key
     */
    public static void popKey() {
        SECRET = backup_secret;
    }

    /**
     * pushSalt() allows you to hold the current salt being used into a holding location so that it can be retrieved later
     * The use case is for when you need to load a new salt, but still want to restore the old one.
     */
    public static void pushSalt() {
        backup_salt = SALT;
    }

    /**
     * This takes the value previously saved by pushSalt() and activates it as the current salt
     */
    public static void popSalt() {
        SALT = backup_salt;
    }

    protected String encrypt(String value) {

        try {
            final byte[] bytes = value != null ? value.getBytes(StandardCharsets.UTF_8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SECRET));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(Base64Support.encode(pbeCipher.doFinal(bytes), Base64Support.NO_WRAP), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected String decrypt(String value) {
        try {
            final byte[] bytes = value != null ? Base64Support.decode(value, Base64Support.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SECRET));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(pbeCipher.doFinal(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Warning, could not decrypt the value.  It may be stored in plaintext.  " + e.getMessage());
            return value;
        }
    }
}
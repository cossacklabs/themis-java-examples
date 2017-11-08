package de.test.themistestapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.cossacklabs.themis.InvalidArgumentException;
import com.cossacklabs.themis.NullArgumentException;
import com.cossacklabs.themis.SecureCell;
import com.cossacklabs.themis.SecureCellData;
import com.cossacklabs.themis.SecureCellException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MainActivitySecureCell extends AppCompatActivity {

    Charset charset = StandardCharsets.UTF_8;
    String pass = "pass";
    byte[] passKey = pass.getBytes(charset);

    String message = "hello message";
    byte[] context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check secure cell
        try {

            SecureCell sc = new SecureCell(passKey, SecureCell.MODE_SEAL);

            // it's working
            workingEncryption(sc);

            // decrypt data encrypted by this code
            workingDecryption(sc);

            // decrypt data encrypted by any other code (e.g. Java)
            notWorkingDecryption(sc);

        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (NullArgumentException e) {
            e.printStackTrace();
        } catch (SecureCellException e) {
            e.printStackTrace();
        }

    }

    void workingEncryption(SecureCell sc) throws SecureCellException, NullArgumentException {
        SecureCellData protectedData = sc.protect(passKey, context, message.getBytes(charset));
        String encodedString = Base64.encodeToString(protectedData.getProtectedData(), Base64.NO_WRAP);
        Log.d("SMC", "encrypted string = " + encodedString);
    }

    void workingDecryption(SecureCell sc) throws SecureCellException, NullArgumentException, InvalidArgumentException {
        // you can get similar string when run `workingEncryption` method

        String encodedFromAndroid = "AAEBQAwAAAAQAAAADQAAABYHwshhi1OpIFEPSmst338IIJqkrhQkrp9nOHDbWCcs4TSBIX1JT5g1";

        byte[] decodedString = Base64.decode(encodedFromAndroid, Base64.NO_WRAP);
        SecureCellData protectedDataAgain = new SecureCellData(decodedString, null);

        byte[] unprotected = sc.unprotect(passKey, context, protectedDataAgain);
        String decryptedData = new String(unprotected, charset);
        Log.d("SMC", "decrypted data = " + decryptedData);
    }


    void notWorkingDecryption(SecureCell sc) throws SecureCellException, NullArgumentException, InvalidArgumentException {
        // you can get this string from Java example (external code)

        String encodedFromJavaCode = "AAEBQAwAAAAQAAAADwAAAH8oJ4X48l+E7V5CZtk+uO99JZb7enzxuCoLOrG8nW9Xzkep5+QyP+Se6Qo=";
        byte[] decodedString = Base64.decode(encodedFromJavaCode, Base64.NO_WRAP);
        SecureCellData protectedDataAgain = new SecureCellData(decodedString, null);

        byte[] unprotected = sc.unprotect(passKey, context, protectedDataAgain);
        String decryptedData = new String(unprotected, charset);
        Log.d("SMC", "decrypted data = " + decryptedData);
    }
}

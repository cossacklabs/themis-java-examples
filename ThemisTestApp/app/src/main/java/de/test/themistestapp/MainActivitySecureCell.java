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

import java.io.UnsupportedEncodingException;

public class MainActivitySecureCell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check secure cell
        try {
            String password = "pass";
            String CHARSET = "UTF-8";

            //byte[] passwordBytes = new byte[] { 0x70, 0x61, 0x73, 0x73 };
            byte[] passwordBytesFromString = password.getBytes(CHARSET);

            SecureCell cell = new SecureCell(passwordBytesFromString, SecureCell.MODE_SEAL);

            String myData = "data :)";
            String context = "context1";

            byte[] myDataBytesFromString = myData.getBytes(CHARSET);
            //byte[] myDataBytes = new byte[] { 0x61, 0x62, 0x63, 0x61, 0x62, 0x63 };

            byte[] contextBytesFromString = context.getBytes(CHARSET);
            //byte[] contextBytes = new byte[] { 0x61, 0x62, 0x63 };

            SecureCellData cellData = cell.protect(passwordBytesFromString, contextBytesFromString, myDataBytesFromString);
            byte[] protectedData = cellData.getProtectedData();

            String base64EncryptedDataString = Base64.encodeToString(protectedData, Base64.DEFAULT);
            Log.d("SMC", "encrypted string = " + base64EncryptedDataString);

            // TRY WITH SIMULATOR
            // string from simulator, not working :(
            String base64FromSim = "AAEBQAwAAAAQAAAAEAAAAGPvKPAA99EEOUIjiKbIHfbGAJTJ2Yri/nfkVQ10VyV08XUI6Sp5x+MVa8BQ";
            byte[] encryptedData = Base64.decode(base64FromSim, Base64.NO_WRAP);

            //byte[] encryptedData = Base64.decode(base64EncryptedDataString, Base64.NO_WRAP);
            SecureCellData encrypted = new SecureCellData(encryptedData, null);
            Log.d("SMC", "encrypted data = " + encrypted);

            byte[] decryptedData = cell.unprotect(passwordBytesFromString, contextBytesFromString, encrypted);
            Log.d("SMC", "decrypted data = " + decryptedData);

            String base64DecryptedDataString = new String(decryptedData, CHARSET);
            Log.d("SMC", "decrypted string = " + base64DecryptedDataString);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (NullArgumentException e) {
            e.printStackTrace();
        } catch (SecureCellException e) {
            e.printStackTrace();
        }

    }
}

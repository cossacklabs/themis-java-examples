package de.test.themistestapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cossacklabs.themis.NullArgumentException;
import com.cossacklabs.themis.SecureMessageWrapException;

import java.io.IOException;

    public class MainActivitySecureMessage extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Check secure message
            try {
                SecMessageExampleClient.SMessageCIClientTest();
            } catch (NullArgumentException e) {
                e.printStackTrace();
            } catch (SecureMessageWrapException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

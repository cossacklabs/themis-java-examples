package com.cossacklabs.themis.android.example;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.cossacklabs.themis.NullArgumentException;
import com.cossacklabs.themis.PrivateKey;
import com.cossacklabs.themis.PublicKey;
import com.cossacklabs.themis.SecureMessage;
import com.cossacklabs.themis.SecureMessageWrapException;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

// ---------------------- IMPORTANT SETUP ---------------------------------------

// User id and Server Public Key should be copied from the Server Setup Page
// https://themis.cossacklabs.com/interactive-simulator/setup/
// Server public key ("server key"),

public class SecMessageExampleClient {

    public static String ClientPrivateKey = "UkVDMgAAAC1EvnquAPUxxwJsoJxoMAkEF7c06Fo7dVwnWPnmNX5afyjEEGmG";
    public static String ClientPublicKey = "VUVDMgAAAC18urRTA1H1hts93vlLXX59OuyVnY1tGFxl/F3PkhDtzrdQETMi";
    public static String ServerPublicKey = "VUVDMgAAAC1FJv/DAmg8/L1Pl5l6ypyRqXUU9xQQaAgzfRZ+/gsjqgEdwXhc";

    public static String Message = "meow";
    public static String URL = "https://themis.cossacklabs.com/api/sjSwNQuZIaqsLJt/";

    public static void PostRequest(String url, String message, final CallbackListener<byte[]> listener) throws IOException {
        AsyncTask<String, Void, byte[]> task = new AsyncTask<String, Void, byte[]>() {
            @Override
            protected byte[] doInBackground(String... strings) {
                String httpsURL = strings[0];
                String message = strings[1];

                try {
                    String query = "message="+URLEncoder.encode(message,"UTF-8");
                    //String query = "message="+message;

                    URL url = new URL(httpsURL);

                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-length", String.valueOf(query.length()));
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    outputStream.close();

                    connection.connect();

                    String m = connection.getResponseMessage();
                    Log.d("SMC", "getResponseMessage = " + m);

                    InputStream inputStream;

                    // get stream
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        inputStream = connection.getInputStream();
                    } else {
                        inputStream = connection.getErrorStream();
                    }

                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }

                    buffer.flush();
                    return buffer.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                listener.onComplete(bytes);
            }
        };

        task.execute(url, message);
    }

    public static void SMessageCIClientTest() throws NullArgumentException, SecureMessageWrapException, IOException {

        PrivateKey privateKey = new PrivateKey(Base64.decode(ClientPrivateKey.getBytes("UTF-8"), Base64.NO_WRAP));
        PublicKey publicKey = new PublicKey(Base64.decode(ServerPublicKey.getBytes("UTF-8"), Base64.NO_WRAP));
        Log.d("SMC", "privateKey1 = " + Arrays.toString(privateKey.toByteArray()));
        Log.d("SMC", "publicKey1 = " + Arrays.toString(publicKey.toByteArray()));

        final SecureMessage sm = new SecureMessage(privateKey, publicKey);

        byte[] wrappedMessage = sm.wrap(Message.getBytes("UTF-8"));
        String EncodedMessage = Base64.encodeToString(wrappedMessage, Base64.NO_WRAP);
        Log.d("SMC", "EncodedMessage = " + EncodedMessage);

        // from iOS
        //EncodedMessage = "ICcEJjgAAAAAAQFADAAAABAAAAAEAAAAfMHW4JAVNfHuuzDCQyk9iVbS7wpcIqP7Uw4EGkBUTZ8=";

        PostRequest(URL, EncodedMessage, new CallbackListener<byte[]>() {
            @Override
            public void onComplete(byte[] value) {
                try {
                    String unwrappedResult = new String(sm.unwrap(value), StandardCharsets.UTF_8);
                    System.out.println(unwrappedResult);
                    Log.d("SMC", "unwrappedResult = " + unwrappedResult);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
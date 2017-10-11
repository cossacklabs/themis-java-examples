package de.test.themistestapp;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import javax.net.ssl.*;


import com.cossacklabs.themis.*;

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

                    // parse stream
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp, response = "";
                    while ((temp = bufferedReader.readLine()) != null) {
                        response += temp;
                    }
                    return response.getBytes("UTF-8");

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

//    public static String clientPrivateKey1 = "UkVDMgAAAC1c3xE6APxYe3SHAzQKxK0JSbfZ9+NCaIP0CBowNaR0W5fPo73T";
//    public static String clientPublicKey1 = "VUVDMgAAAC0IPC1eA0jOwhRlyzcJOfLkazKMjXpNBPjxFzfaBKxXk0ak3t9g";
//
//    public static String clientPrivateKey2 = "UkVDMgAAAC125EhOAPjRneVidNwhm1J2wedttMz4PAw4u1RRX+Suv3xmcSQS";
//    public static String clientPublicKey2 = "VUVDMgAAAC26OonuAzes2JBJSXvNCMiiTf3qsAD4Ae0nyQ2uEd5JNmvSjp9r";

//    public static void SMessageCIClientTest() throws NullArgumentException, SecureMessageWrapException, IOException {
//
//        PrivateKey privateKey1 = new PrivateKey(Base64.decode(clientPrivateKey1.getBytes("UTF-8"), Base64.NO_WRAP));
//        PublicKey publicKey1 = new PublicKey(Base64.decode(clientPublicKey1.getBytes("UTF-8"), Base64.NO_WRAP));
//        PrivateKey privateKey2 = new PrivateKey(Base64.decode(clientPrivateKey2.getBytes("UTF-8"), Base64.NO_WRAP));
//        PublicKey publicKey2 = new PublicKey(Base64.decode(clientPublicKey2.getBytes("UTF-8"), Base64.NO_WRAP));
//
//
//        final SecureMessage sm1 = new SecureMessage(privateKey1, publicKey2);
//        final SecureMessage sm2 = new SecureMessage(privateKey2, publicKey1);
//
//        byte[] wrappedMessage = sm1.wrap(Message.getBytes("UTF-8"));
//        String wrappedMessageA = Base64.encodeToString(wrappedMessage, Base64.NO_WRAP);
//
//        byte[] unwrappedMessage = sm2.unwrap(wrappedMessage);
//        String unwrappedMessageA = new String(unwrappedMessage, StandardCharsets.UTF_8);
//
//        Log.d("SMC", "original message = " + Message);
//        Log.d("SMC", "wrappedMessageA = " + wrappedMessageA);
//        Log.d("SMC", "unwrappedMessageA = " + unwrappedMessageA);
//
//
////        String EncodedMessage = Base64.encodeToString(wrappedMessage, Base64.NO_WRAP);
////
////        PostRequest(URL, EncodedMessage, new CallbackListener<byte[]>() {
////            @Override
////            public void onComplete(byte[] value) {
////                try {
////                    String unwrappedResult = new String(sm.unwrap(value), StandardCharsets.UTF_8);
////                    System.out.println(unwrappedResult);
////                    Log.d("SMC", "unwrappedResult = " + unwrappedResult);
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        });
//    }
}
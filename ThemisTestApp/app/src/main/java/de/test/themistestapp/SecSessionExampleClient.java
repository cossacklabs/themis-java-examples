package de.test.themistestapp;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.cossacklabs.themis.*;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;


public class SecSessionExampleClient {

    public static String ClientId = "ULzsfYGzScUisNV";
    public static String ServerId = "mLAResUZAQcQXXl";

    public static String ClientPrivateKey = "UkVDMgAAAC1EvnquAPUxxwJsoJxoMAkEF7c06Fo7dVwnWPnmNX5afyjEEGmG";
    public static String ClientPublicKey = "VUVDMgAAAC18urRTA1H1hts93vlLXX59OuyVnY1tGFxl/F3PkhDtzrdQETMi";
    public static String ServerPublicKey = "VUVDMgAAAC0boM1EAvAkoWsfqbMvugv/YzzMPC6AeKT/t5gtCb3xyPpEJJB/";

    public static String Message = "msg msg msg :)";
    public static String URL = "https://themis.cossacklabs.com/api/ULzsfYGzScUisNV/";

    public static String CHARSET = "UTF-8";


    public static void PostRequest(String url, String message, final CallbackListener<byte[]> listener) throws IOException {
        AsyncTask<String, Void, byte[]> task = new AsyncTask<String, Void, byte[]>() {
            @Override
            protected byte[] doInBackground(String... strings) {
                String httpsURL = strings[0];
                String message = strings[1];

                try {
                    String query = "message="+ URLEncoder.encode(message,"UTF-8");
                    //String query = "message="+message;

                    java.net.URL url = new URL(httpsURL);

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

    public static void startSession(final SecureSession ss, String base64MessageToSend) throws IOException {
        PostRequest(URL, base64MessageToSend, new CallbackListener<byte[]>() {
            @Override
            public void onComplete(byte[] value) {
                try {
                    byte[] unwrappedData = ss.unwrap(value).getData();
                    String unwrappedResult = new String(unwrappedData, StandardCharsets.UTF_8);
                    System.out.println(unwrappedResult);
                    Log.d("SMC", "session establish in progress = " + unwrappedResult);
                    String messageToSend2 = Base64.encodeToString(unwrappedData, Base64.NO_WRAP);

                    if (!ss.isEstablished()) {
                        startSession(ss, messageToSend2);
                    } else {
                        sendRealMessage(ss);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void sendRealMessage(final SecureSession ss) throws IOException, SecureSessionException, NullArgumentException {
        byte[] wrappedMessage = ss.wrap(Message.getBytes("UTF-8"));
        String base64Message = Base64.encodeToString(wrappedMessage, Base64.NO_WRAP);

        PostRequest(URL, base64Message, new CallbackListener<byte[]>() {
            @Override
            public void onComplete(byte[] value) {
                try {
                    byte[] unwrappedData = ss.unwrap(value).getData();
                    String unwrappedResult = new String(unwrappedData, StandardCharsets.UTF_8);
                    System.out.println(unwrappedResult);
                    Log.d("SMC", "message received = " + unwrappedResult);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void SSessionCIClientTest() throws NullArgumentException, SecureMessageWrapException, IOException, SecureSessionException{
        ISessionCallbacks callbacks = new ISessionCallbacks() {

            @Override
            public PublicKey getPublicKeyForId(SecureSession session, byte[] id) {

                try {
                    byte[] serverId = ServerId.getBytes(CHARSET);
                    if (Arrays.equals(id, serverId)) {
                        PublicKey publicKey = new PublicKey(Base64.decode(ServerPublicKey.getBytes("UTF-8"), Base64.NO_WRAP));
                        Log.d("SMC", "publicKey1 = " + Arrays.toString(publicKey.toByteArray()));
                        return publicKey;
                    }
                } catch (Exception e) {};
                return null;
            }

            @Override
            public void stateChanged(SecureSession session) {
            }
        };

        PrivateKey privateKey = new PrivateKey(Base64.decode(ClientPrivateKey.getBytes("UTF-8"), Base64.NO_WRAP));
        Log.d("SMC", "privateKey1 = " + Arrays.toString(privateKey.toByteArray()));

        final SecureSession ss = new SecureSession(ClientId.getBytes(CHARSET), privateKey, callbacks);

        final String messageToSend = Base64.encodeToString(ss.generateConnectRequest(), Base64.NO_WRAP);
        startSession(ss, messageToSend);
    }
}
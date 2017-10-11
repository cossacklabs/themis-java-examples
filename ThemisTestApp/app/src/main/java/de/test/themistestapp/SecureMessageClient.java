package de.test.themistestapp;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.cossacklabs.themis.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class SecureMessageClient {
    public static void PostRequest(String url, String message, final CallbackListener<byte[]> listener) throws IOException {
        AsyncTask<String, Void, byte[]> task = new AsyncTask<String, Void, byte[]>() {
            @Override
            protected byte[] doInBackground(String... strings) {
                String httpsURL = strings[0];
                String message = strings[1];

                try {
                    String query = "message="+ URLEncoder.encode(message,"UTF-8");
                    URL url = new URL(httpsURL);

                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-length", String.valueOf(query.length()));
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                    output.writeBytes(query);
                    output.close();

                    if (connection .getResponseCode() != 200) {
                        throw new IOException();
                    }

                    DataInputStream input = new DataInputStream(connection.getInputStream());
                    int responseBytesCapacity = 5 * 1024;
                    byte[] responseBytes = new byte[responseBytesCapacity];
                    int currentPosition = 0;
                    int responseLength = 0;
                    int readBytes = -1;

                    while((readBytes = input.read(responseBytes,currentPosition, responseBytesCapacity - responseLength)) !=- 1) {
                        if((responseLength + readBytes) > responseBytesCapacity) {
                            throw new IOException();
                        }

                        responseLength += readBytes;
                        currentPosition += readBytes;
                    }

                    input.close();

                    byte[] response = new byte[responseLength];

                    for(int i=0; i<responseLength; ++i) {
                        response[i] = responseBytes[i];
                    }

                    return response;
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

    public static void SMessageCIClientTest() throws NullArgumentException, SecureMessageWrapException, IOException{
//        Keypair pair = null;
//
//        try {
//            pair = KeypairGenerator.generateKeypair(AsymmetricKey.KEYTYPE_EC);
//        } catch (KeyGenerationException | InvalidArgumentException e) {
//            e.printStackTrace();
//        }

        String serverPublicKey = "VUVDMgAAAC3AGRmEA0y4uWUD7pHHKYPIu8oZCc97QSp2F+cZGWopuKZwS/aP";
        String clientId = "nPROjiVaViKaUtB";
        String message = "android";
        String url = "https://themis.cossacklabs.com/api/"+clientId+"/";

        // Uncomment the following to generate a new Keypair with Android Base64 Encoding

        //String clientPrivateKeyAndroid = Base64.encodeToString(pair.getPrivateKey().toByteArray(), Base64.NO_WRAP);
        //String clientPublicKeyAndroid = Base64.encodeToString(pair.getPublicKey().toByteArray(), Base64.NO_WRAP);

        // Uncomment the following to generate a new Keypair with Java Base64 Encoding

        //String clientPrivateKeyJava = java.util.Base64.getEncoder().encodeToString(pair.getPrivateKey().toByteArray());
        //String clientPublicKeyJava = java.util.Base64.getEncoder().encodeToString(pair.getPublicKey().toByteArray());


        // Keypair with Android Base64 Encoding
        String clientPrivateKeyAndroid = "UkVDMgAAAC1EvnquAPUxxwJsoJxoMAkEF7c06Fo7dVwnWPnmNX5afyjEEGmG";//"UkVDMgAAAC3sG8dEANszV+NVn9cOYZI+Yy0sm++37wznwk0oRcLixj+QWqpw";
        String clientPublicKeyAndroid = "VUVDMgAAAC18urRTA1H1hts93vlLXX59OuyVnY1tGFxl/F3PkhDtzrdQETMi";//"VUVDMgAAAC1PpsCRAl6HC9asFhiunFzfEt2U+SmW6XO8sNZ911XfXtEny4ND";
        Log.d("SMC", "Private key android " + clientPrivateKeyAndroid);
        Log.d("SMC", "Public key android " + clientPublicKeyAndroid);

//        // Keypair with Java Base64 Encoding
//        String clientPrivateKeyJava = "UkVDMgAAAC3H2DL2AK7zkKgjV4RvI5N+MUnldbHf/blQT8Bs3PO/5vTxpHQs";//"UkVDMgAAAC2jZixCAO/6TDFyQpgJw/8B9D1s7TsqSUG8G4zoXHj4KvhGP62Q";
//        String clientPublicKeyJava = "VUVDMgAAAC1twpZ7Agic5IKANZ3zlwG5Y5g3CDeKiAPZsH2KoBK/ikQdXGh1"; //"VUVDMgAAAC3X5iaMAuux4FoDMtXfAkUXN9fGPh+nC+4d7GIgXUrrt1Mg8pWh";
//        Log.d("SMC", "Private key java" + clientPrivateKeyJava);
//        Log.d("SMC", "Public key java" + clientPublicKeyJava);

//        PrivateKey privateKey1 = new PrivateKey(clientPrivateKeyAndroid.getBytes("UTF-8"));
//        PublicKey publicKey1 = new PublicKey(serverPublicKey.getBytes("UTF-8"));


        PrivateKey privateKey1 = new PrivateKey(Base64.decode(clientPrivateKeyAndroid, Base64.DEFAULT));
        PublicKey publicKey1 = new PublicKey(Base64.decode(serverPublicKey, Base64.DEFAULT));
        Log.d("SMC", "privateKey1 = " + Arrays.toString(privateKey1.toByteArray()));
        Log.d("SMC", "publicKey1 = " + Arrays.toString(publicKey1.toByteArray()));

        final SecureMessage secureMessageAndroid = new SecureMessage(privateKey1, publicKey1);

        byte[] wrappedMessage = secureMessageAndroid.wrap(message.getBytes("UTF-8"), publicKey1);
        String encodedMessageAndroid = Base64.encodeToString(wrappedMessage, Base64.DEFAULT);
        Log.d("SMC", "encodedMessageAndroid = " + encodedMessageAndroid);


        // old:
        //final SecureMessage secureMessageAndroid = new SecureMessage(new PrivateKey(Base64.decode(clientPrivateKeyAndroid, Base64.NO_WRAP)), new PublicKey(Base64.decode(serverPublicKey, Base64.NO_WRAP)));
        //final SecureMessage secureMessageJava = new SecureMessage(new PrivateKey(java.util.Base64.getDecoder().decode(clientPrivateKeyAndroid)), new PublicKey(java.util.Base64.getDecoder().decode(serverPublicKey)));
        //String encodedMessageAndroid = Base64.encodeToString(secureMessageAndroid.wrap(message.getBytes("UTF-8")), Base64.NO_WRAP);

        //String encodedMessageJava = java.util.Base64.getEncoder().encodeToString(secureMessageAndroid.wrap(message.getBytes("UTF-8")));
        Log.d("SMC", "encoded message android " + encodedMessageAndroid);
        //Log.d("SMC", "encoded message java " + encodedMessageJava);

        PostRequest(url, encodedMessageAndroid, new CallbackListener<byte[]>() {
            @Override
            public void onComplete(byte[] value) {
                try {
                    System.out.println(new String(secureMessageAndroid.unwrap(value), "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        PostRequest(url, encodedMessageJava, new CallbackListener<byte[]>() {
//            @Override
//            public void onComplete(byte[] value) {
//                try {
//                    System.out.println(new String(secureMessageJava.unwrap(value), "UTF-8"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}

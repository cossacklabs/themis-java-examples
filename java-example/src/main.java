import com.cossacklabs.themis.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;


public class main {

  static Charset charset = StandardCharsets.UTF_8;
  static String pass = "pass";
  static byte[] passKey = pass.getBytes(charset);

  static String message = "hello from java";
  static byte[] context = null;

  public static void main(final String[] args) throws NullArgumentException, SecureMessageWrapException, IOException, SecureSessionException, SecureCellException, InvalidArgumentException {
    //SMessageClient.SMessageCIClientTest();
    //SSessionClient.SSessionCIClientTest();
//    checkLocalSecureMessage();


    SecureCell sc = new SecureCell(passKey);

    workingEncryption(sc);
    workingDecryption(sc);
    notWorkingDecryption(sc);

  }

  static void workingEncryption(SecureCell sc) throws SecureCellException, NullArgumentException {
    SecureCellData protectedData = sc.protect(passKey, context, message.getBytes(charset));
    String encodedString = Base64.getEncoder().encodeToString(protectedData.getProtectedData());
    System.out.println("data = " + encodedString);
  }

  static void workingDecryption(SecureCell sc) throws SecureCellException, NullArgumentException, InvalidArgumentException {
    // you can get similar string when run `workingEncryption` method

    String encodedFromJavaCode = "AAEBQAwAAAAQAAAADwAAAH8oJ4X48l+E7V5CZtk+uO99JZb7enzxuCoLOrG8nW9Xzkep5+QyP+Se6Qo=";
    byte[] decodedString = Base64.getDecoder().decode(encodedFromJavaCode);
    SecureCellData protectedDataAgain = new SecureCellData(decodedString, null);

    byte[] unprotected = sc.unprotect(passKey, context, protectedDataAgain);
    String str = new String(unprotected);
    System.out.println(str);
  }


  static void notWorkingDecryption(SecureCell sc) throws SecureCellException, NullArgumentException, InvalidArgumentException {
    // you can get this string from Java example (external code)

    String encodedFromAndroid = "AAEBQAwAAAAQAAAADQAAABYHwshhi1OpIFEPSmst338IIJqkrhQkrp9nOHDbWCcs4TSBIX1JT5g1";
    byte[] decodedString = Base64.getDecoder().decode(encodedFromAndroid);
    SecureCellData protectedDataAgain = new SecureCellData(decodedString, null);

    byte[] unprotected = sc.unprotect(passKey, context, protectedDataAgain);
    String str = new String(unprotected);
    System.out.println(str);
  }


  static void checkLocalSecureMessage() throws UnsupportedEncodingException, NullArgumentException, SecureMessageWrapException {
    Charset charset = StandardCharsets.UTF_8;
    String clientPrivateKey = "UkVDMgAAAC1EvnquAPUxxwJsoJxoMAkEF7c06Fo7dVwnWPnmNX5afyjEEGmG";
    String serverPublicKey = "VUVDMgAAAC1FJv/DAmg8/L1Pl5l6ypyRqXUU9xQQaAgzfRZ+/gsjqgEdwXhc";

    String message = "some weird message here";

    PrivateKey privateKey = new PrivateKey(Base64.getDecoder().decode(clientPrivateKey.getBytes(charset.name())));
    PublicKey publicKey = new PublicKey(Base64.getDecoder().decode(serverPublicKey.getBytes(charset.name())));
    System.out.println("privateKey1 = " + Arrays.toString(privateKey.toByteArray()));
    System.out.println("publicKey1 = " + Arrays.toString(publicKey.toByteArray()));

    final SecureMessage sm = new SecureMessage(privateKey, publicKey);

    byte[] wrappedMessage = sm.wrap(message.getBytes(charset.name()));
    String encodedMessage = Base64.getEncoder().encodeToString(wrappedMessage);
    System.out.println("EncodedMessage = " + encodedMessage);

    byte[] wrappedMessageFromB64 = Base64.getDecoder().decode(encodedMessage);
    String decodedMessage = new String(sm.unwrap(wrappedMessageFromB64), charset);
    System.out.println("DecodedMessage = " + decodedMessage);

    String encodedMessageFromExternal = "ICcEJksAAAAAAQFADAAAABAAAAAXAAAAnner6w5kgaRH5O8Xb8C08zu+EqJuaAPNH7bzXtoq/6xTmQkp9kfdOqDfsbXaDSpd3kYG";
    byte[] wrappedMessageFromB64External = Base64.getDecoder().decode(encodedMessageFromExternal);
    String decodedMessageExternal = new String(sm.unwrap(wrappedMessageFromB64External), charset);
    System.out.println("DecodedMessage = " + decodedMessageExternal);
  }
}

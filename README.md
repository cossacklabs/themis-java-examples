Desktop Java and Android Java simple example projects.

This are demo projects that enhance documentation for Java and Android that you can find on wiki:

https://github.com/cossacklabs/themis/wiki/Java-and-Android-Howto


# How to run?

### Java example

1. Open `java-example`, import as IDEA project.
2. Included library: `lib/libthemis_jni.dylib`
3. Included themis/java source: `src/com/cossacklabs/themis`
4. Run `main.java`.

### Android example

1. Open `ThemisTestApp`, import as Android Studio project.
2. Included library: `app/libs/themis-release.aar`, linked by gradle (inside `app/build.gradle`)
2. Run `Secure Cell`.

# What are these examples?

Client code (both Android and Java) contains simple example for symmetric and asymmetric encryption.

1. Symmetric encryption using Themis Secure Cell. Open `main` file for Java and `MainActivitySecureCell` for Android.

```
encryptDataForStoring()
```

1.1 Create SecureCell with desired password:
 
```
SecureCell sc = new SecureCell(passKey);
```

1.2. Encrypt data. You will get base64 encoded string as output in console.

```
SecureCellData encryptedData = sc.protect(password, optionalContext, message);
String encryptedDataString = Base64.getEncoder().encodeToString(encryptedData.getProtectedData());
```

1.3. Decrypt data previously encrypted. You will get original message as output in console.

```
byte[] decodedEncryptedString = Base64.getDecoder().decode(encryptedDataString);
SecureCellData encryptedDataFromString = new SecureCellData(decodedEncryptedString, null);

byte[] unprotected = sc.unprotect(password, optionalContext, encryptedDataFromString);
String decryptedString = new String(unprotected);
```

2. Asymmetric encryption using Themis Secure Message.

2.1 For Java, see `main` file:

```
encryptDataForMessaging
```

2.2 For Android, see `MainActivitySecureMessage` file:

```
secureMessageLocal
```


# Themis Interactive simulator

Both examples contains ready-to-use solutions to test asymmetric encryption with Themis Interactive Server. No need to run your own server to check if you have implemented encryption correctly.

For Java check `SMessageClient` and `SSessionClient`. For Android check `MainActivitySecureMessage` and `MainActivitySecureSession`.

Comprehenvise documentation can be found below: https://themis.cossacklabs.com/interactive-simulator/setup/


# How to build Themis library manually?

1. Copy latest Java wrapper from ([themis/src/wrappers/themis/java](https://github.com/cossacklabs/themis/tree/master/src/wrappers/themis/java/com/cossacklabs/themis)). Paste into `java-example/src` folder.

2. Clone and compile BoringSSL for your machine according instructions in the [themis -> Building and Installing -> BoringSSL](https://github.com/cossacklabs/themis/wiki/Building-and-installing#boringssl) section. 

3. Compile `themis_jni` that uses BoringSSL according instructions in the [themis -> Java and Android Howto](https://github.com/cossacklabs/themis/wiki/Java-and-Android-Howto#building-themis-for-java) section.

   Now you have `libthemis_jni` shared library located in the themis folder `build_with_boringssl_jni` folder. Copy library and put into `java-example/lib` folder.

4. Compile BoringSLL for android architectures, check instructions in the [themis -> Building and Installing -> Android](https://github.com/cossacklabs/themis/wiki/Building-and-installing#android) section. 

5. Build `themis.aar` archive:
https://github.com/cossacklabs/themis/wiki/Building-and-installing#android

  You will get it in the themis folder `build/outputs/aar/`. Copy archive into `ThemisTestApp/app/libs/` folder and rename to `themis-release.aar`

6. Now you can run this :)


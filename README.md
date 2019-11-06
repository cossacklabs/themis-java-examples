Desktop Java and Android Java simple example projects.

This are demo projects that enhance documentation for Java and Android:

https://docs.cossacklabs.com/pages/java-and-android-howto/

# How to run?

### Java example

1. Install Themis Core ([see below](#for-desktop-java)).
2. Open `java-example` in IntelliJ IDEA.
3. Run `main.java`.

### Android example

1. Open `android-example`, import as Android Studio project.
2. Included library Themis for Android via bintray maven repository (see below).
3. Run `MainActivitySecureCell` as secure data storage or `MainActivitySecureMessage` to see secure messaging example.

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

Comprehenvise documentation can be found below: https://docs.cossacklabs.com/simulator/interactive/


# How to install Themis

### For Desktop Java

Normally you will only need to install Themis Core [from repositories](https://docs.cossacklabs.com/pages/documentation-themis/#installing-themis-core). Major operating systems are supported.

This example project already includes prebuilt Themis JNI library for modern 64-bit Linux and macOS systems.

If this does not work, you may need to build Themis from source code:
1. Follow [these instructions](https://docs.cossacklabs.com/pages/documentation-themis/#java-wrapper-installation) to build Themis JNI library.
2. Copy JavaThemis source code (`src/wrappers/themis/java`) into the `java-example/src` directory.
3. Copy Themis JNI library (`build/libthemis_jni.*`) into the `java-example/lib` directory.

### For Android

1. Add bintray repository into your repositories from **`build.gradle`**

```
repositories {
    google()
    jcenter()
    maven { url "https://dl.bintray.com/cossacklabs/maven/" }
}
```

2. Link to themis from **`app/build.gradle`**

```
dependencies {
     // ....
    implementation 'com.cossacklabs.com:themis:+' // better to check which version is current latest and link to it
}
```

Desktop Java and Android Java simple example projects.

These are demo projects that enhance documentation for Java and Android:

https://docs.cossacklabs.com/pages/java-and-android-howto/

# How to run?

### Java example

1. Install Themis Core ([see below](#for-desktop-java)).
2. Open `java-example` in IntelliJ IDEA.
3. Run `main.java`.

_Note_: if you want to use bundled (or copied) `lib/libthemis_jni.{so or dylib}`
you will have to add `LD_LIBRARY_PATH=lib` (even better, use the absolute path to `lib`)
to environment variables of the `Application` `main` configuration, so the JVM
will be able to find it.

If you cannot run the example app because of error like this
```
Exception in thread "main" java.lang.UnsatisfiedLinkError: <some path>/libthemis_jni.so: libthemis.so.0: cannot open shared object file: No such file or directory
```
you will have to add `/usr/local/lib` (or wherever the `libthemis` is stored)
to the same `LD_LIBRARY_PATH` variable. You can combine two directories like this:
`LD_LIBRARY_PATH=lib:/usr/local/lib` (just separate with `:`).
This magic variable may be used on both Linux and macOS.

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
1. Follow [these instructions](https://docs.cossacklabs.com/themis/languages/java/installation-desktop/) to build Themis JNI library.
   You will also need to build the JAR.
2. Copy Themis JNI library (`build/libthemis_jni.*`) into the `java-example/lib` directory.
3. Copy Themis JAR (`src/wrappers/themis/java/build/libs/java-themis-*.jar`)
   into the `java-example/lib` directory and make sure `build.gradle` contains the same file name.
   Both `classpath` and `implementation` lines should be updated.

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

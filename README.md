# Themis examples for Java and Android

Desktop Java and Android Java simple example projects.

These are demo projects that enhance documentation for Java and Android:

https://docs.cossacklabs.com/pages/java-and-android-howto/

## How to run?

### Java example

1. Install Themis Core and Themis JNI library ([see below](#for-desktop-java)).
2. Open `java-example` in IntelliJ IDEA.
3. Run `main.java`.

_Note_: If you cannot run the example app because of error like this
```
Exception in thread "main" java.lang.UnsatisfiedLinkError: <some path>/libthemis_jni.so: libthemis.so.0: cannot open shared object file: No such file or directory
```
you will have to add `LD_LIBRARY_PATH=/usr/local/lib` (or wherever the `libthemis` is stored)
to environment variables of the `Application` `main` configuration, so the JVM
will be able to find it. This magic variable may be used on both Linux and macOS.

If `libthemis_jni.so` is the one it cannot find, you may want to try adding JVM
option `java.library.path=/path/to/dir_with_libthemis_jni.so`.

### Android example

1. Open `android-example`, import as Android Studio project.
2. Included library Themis for Android via bintray maven repository (see below).
3. Run `MainActivitySecureCell` as secure data storage or `MainActivitySecureMessage` to see secure messaging example.

## What are these examples?

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


## Themis Interactive simulator

Both examples contain ready-to-use solutions to test asymmetric encryption with Themis Interactive Server.
No need to run your own server to check if you have implemented encryption correctly.

For Java check `SMessageClient` and `SSessionClient`. For Android check `MainActivitySecureMessage` and `MainActivitySecureSession`.

Comprehensive documentation can be found below: https://docs.cossacklabs.com/simulator/interactive/


## How to install Themis

### For Desktop Java

 1. Install Themis Core and Themis JNI libraries.

    Follow [the instructions](https://docs.cossacklabs.com/themis/languages/java/installation-desktop/).
    Linux and macOS are supported.

 2. Add Maven Central repository and use `java-themis` in **`build.gradle`**:

    ```
    repositories {
        mavenCentral()
    }

    dependencies {
        // Add JavaThemis as runtime dependency of your application.
        // Always pin the latest version, you can find it here:
        // https://search.maven.org/artifact/com.cossacklabs.com/java-themis
        implementation 'com.cossacklabs.com:java-themis:0.13.1'
    }
    ```

    Please note that the package name is different from Android.

### For Android

1. Add Maven Central repository to your repositories in **`build.gradle`**

```
repositories {
    google()
    jcenter()
    mavenCentral()
}
```

2. Link to themis from **`app/build.gradle`**

```
dependencies {
    // Add JavaThemis as runtime dependency of your application.
    // Always pin the latest version, you can find it here:
    // https://search.maven.org/artifact/com.cossacklabs.com/themis
    implementation 'com.cossacklabs.com:themis:0.13.1'
}
```

Please note that the package name is different from desktop Java.

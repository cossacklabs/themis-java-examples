Android app and Java code with weird themis secure cell / secure message bug.


Example project illustrating the issue https://github.com/cossacklabs/themis/issues/220

## Problem description

SecureCell is encryption-decryption tool from [themis](https://github.com/cossacklabs/themis). Both Android and Java wrapper share same code.

- themis C crypto code.
- [themis_jni code](https://github.com/cossacklabs/themis/tree/master/jni) (C-level layer between C core and Java code).
- Java wrapper ([src/wrapper/java](https://github.com/cossacklabs/themis/tree/master/src/wrappers/themis/java/com/cossacklabs/themis)).
- client app code (android code or Java code that uses encryption-decryption).

### Problem definition.

Java app and Android app can encrypt-decrypt data they create. Thus, if you encrypt data on Android app, you can decrypt it from Android app. If you encrypt data on Java app, you can decrypt it from Java app. 

However, if you encrypt data on Android, it's impossible to decrypt it on any other platform (Java, Go, Python, etc). Same thing, Android wraper cannot decrypt data encrypted on any other platform. 

Java wrapper encryption-decryption compatibility was checked among other platforms and languages (go, python, ruby, swift) and is known to be compatible with all of them.

So, Android wrapper is the only one that is not compatible with others.


### Possible reasons

These possible reasons were investigated:

- themis version incompatibility. All tests are made on latest themis code (master branch). However, we checked that the problem existed starting from themis 0.9.3.

- encoding problem. The easiest way to send data in/out is to encode it to base64. It might happen that Android uses different base64 encoding that others. However, this is likely possible, because we checked encoded/decoded data byte-per-byte, and it looks the same between Java and Android code.

- BoringSSL compatibility? However, themis_jni is built with boringSSL engine for both Java and Android code. 

- JNI layer compatibility. JNI code might be interpreted differently on Android and Java VMs. Type size problems?

- magic?

### Other

Secure Cell seal mode is a platform-to-test, because this is the most simple configuration. However, SecureMessage wrap-unwrap, and SecureSession connections are not working from Android code as well.


## Set up.

### Java example

1. Open `java-example`, import as IDEA project.
2. Included library: `lib/libthemis_jni.dylib`
3. Included themis/java source: `src/com/cossacklabs/themis`
4. Run `main.java`.

### Android example

1. Open `ThemisTestApp`, import as Android Studio project.
2. Included library: `app/libs/themis-0.9.5-debug.aar`, linked by gradle (inside `app/build.gradle`)
2. Run `Secure Cell`.


## Build manually

1. Copy latest Java wrapper from ([themis/src/wrappers/themis/java](https://github.com/cossacklabs/themis/tree/master/src/wrappers/themis/java/com/cossacklabs/themis)). Paste into `java-example/src` folder.

2. Clone and compile BoringSSL for your machine according instructions in the [themis -> Building and Installing -> BoringSSL](https://github.com/cossacklabs/themis/wiki/Building-and-installing#boringssl) section. 

3. Compile `themis_jni` that uses BoringSSL according instructions in the [themis -> Java and Android Howto](https://github.com/cossacklabs/themis/wiki/Java-and-Android-Howto#building-themis-for-java) section.

   Now you have `libthemis_jni` shared library located in the themis folder `build_with_boringssl_jni` folder. Copy library and put into `java-example/lib` folder.

4. Compile BoringSLL for android architectures, check instructions in the [themis -> Building and Installing -> Android](https://github.com/cossacklabs/themis/wiki/Building-and-installing#android) section. 

5. Build `themis.aar` archive:
https://github.com/cossacklabs/themis/wiki/Building-and-installing#android

  You will get it in the themis folder `build/outputs/aar/`. Copy archive into `ThemisTestApp/app/libs/` folder and rename to `themis-0.9.5-debug.aar`

6. Now you can run this :)



# Execution

Client code (both Android and Java) contains simple example.

1. Create SecureCell with the same password
 
```
SecureCell sc = new SecureCell(passKey);
```

2. Encrypt data. You will get base64 encoded string as output in console.


```
workingEncryption(sc);
```

3. Decrypt data previously encrypted by this platform. You will get original message as output in console.

```
workingDecryption(sc);
```

4. Decrypt data encrypted by another platform. You will get error message. Including line number of internal themis-core function that fails.

```
notWorkingDecryption(sc);
```

# Expected result

Both Android code and Java code can decrypt messages encrypted by each other.

# themis-bug
android app with weird themis secure cell / secure message bug


Example project from issue https://github.com/cossacklabs/themis/issues/220

## Set up

Uses themis-0.9.4.aar build by @iONsky.
Uses Android SDK 26.


## Secure Cell problem

1. Open `MainActivitySecureCell.java`

2. Run Secure Cell example, make sure it's failing

3. Open [Data Simulator](https://themis.cossacklabs.com/data-simulator/cell/), set pass to "pass", context to "context1". Press Encrypt. Copy encrypted string.

4. Place encrypted string to `base64FromSim` var.

5. Run example. See it's failing.

Expected result: encrypted data from Data Simulator can be decrypted by themis-android secure cell.

6. Encrypt data in Android project, copy resulting base64 string. 

7. Put base64 string inside Data Simulator (make sure that you're using the same 'pass' and 'context')

8. Get error 'Themis cell failed decrypting'

Expected result: encrypted data from Android app can be decrypted by Data Simulator.


## Secure Message problem

1. Open `MainActivitySecureMessage.java`

2. Open [Interactive Simulator](https://themis.cossacklabs.com/interactive-simulator/setup/#). Update client ID, client public keys, server key.

4. Run simulator in SecureMessage mode.

5. Run Android app. See 'secure message failed to decrypt' message.
 
Expected result: Secure Message is working fine between Simulator and themis-android.


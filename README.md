# meals-management-system

Meals Management System aims to reduce food waste by suggesting recepies
based on products in your fridge and their expiration date.

## Setup

1. Generate a public/private key pair. You can do it using keytool:

```
keytool -genkeypair -alias [key name] -keyalg RSA -keysize 4096 \
  -validity 3650 -dname "CN=localhost" -keystore keystore.p12 -storeType PKCS12
```

-   You will be prompted to enter a password. Upon entering the password, `keystore.p12`
    will be generated in your current working directory.
-   Place the keystore in the `src\main\resources\keystore` directory
-   In `application.properties` change the `server.ssl.key-store-password`
    property to be compliant with the password you used when generating the keystore

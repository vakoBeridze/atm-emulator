# ATM Emulator

## Overview

- Solution has two services and one database module:
    - `bank-service` provides bank services: Cash deposit, Cash withdrawal, Check balance, etc.
    - `atm-service` simple client for bank-service.
    - `atm-db` postgres database docker image.
- To access `atm-service` two-step authentication is needed, finally `atm-service` generates limited time access token.
    - `/api/authentication/initiate` method checks if card is valid and generates access token which is valid only for accessing finalize method.
    - `/api/authentication/finalize` method validates card preferred authentication method (PIN, fingerprint etc.), and generates access token which is valid for accessing rest of the atm services.
    - After finalizing authentication JWT token payload looks like:
  ```json
  {
    "sub": "1111222233334444",
    "preferred_auth": "FINGERPRINT",
    "roles": [
      "FINALIZED_AUTHENTICATION"
    ],
    "iat": 1640896897,
    "exp": 1640898097
  }
  ```
- To access `bank-service` some `API-KEY` needs to be set as a header.
- `bank-service` automatically creates database schema and loads some sample data. There are about three main tables:
  ![schema.png](./bank-service/src/main/resources/db/schema.png)
- `Hibernate envers` is used for auditing.
- Both of the services have swagger ui configured and can be accessed at `/swagger-ui.html`
- Logging can be configured by setting `LOG_LEVEL` env variable.

## Start ATM Emulator

- Simplest way to start everything is to use `docker-compose` which will start postgres database and two services.

```
docker-compose up
```

- Otherwise, apps can be started using java. (postgres database is required as well)
- Access `atm-service` swagger at: http://localhost:8080/swagger-ui.html
- Access `bank-service` swagger at: http://localhost:8090/swagger-ui.html
- Postman can be also used to test some endpoints. See ready to use postman collection in source files. `ATM Emulator.postman_collection.json`

# ATM Emulator

## Overview

- Solution has two services and one database modules:
  - `bank-service` provides bank services: Cash deposit, Cash withdrawal, Check balance, ...
  - `atm-service` simple client for bank-service.
  - `atm-db` postgres database docker image.
- To access `atm-service` two-step authentication is needed, finally `atm-service` generates limited time access token.
  - `/api/authentication/initiate` method checks if card is valid and generates access token which is valid only for accessing finalize method.
  - `/api/authentication/finalize` method validates card preferred authentication method (PIN, fingerprint etc.), and generates access token which is valid for accessing rest of the atm services.
- To access `bank-service` some API-KEY needs to be set as a header.
- `bank-service` automatically creates database schema and loads some sample data.
- Both of the services have swagger ui configured and can be accessed at `/swagger-ui.html`
- `Hibernate envers` is used for auditing.
- Logging can be configured by setting `LOG_LEVEL` env variable.

## Start ATM Emulator

- Simplest way to start everything is to use `docker-compose` which will start postgres database and two services.

```
docker-compose up
```

- Otherwise, apps can be started using java.
- Access `atm-service` swagger at: http://localhost:8080/swagger-ui.html
- Access `bank-service` swagger at: http://localhost:8090/swagger-ui.html

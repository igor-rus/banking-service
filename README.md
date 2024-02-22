# Banking API

## About the Project
*This project simulates a primitive banking API.*

## Main Features

- Fetching accounts and details
- Depositing to an account
- Withdrawal from an account
- Money transfer between existing accounts

## Technologies

- Java 17
- Spring Boot 3
- Spring Security
- Liquibase
- H2 in-memory DB
- Gradle
- Swagger

## API Structure and Endpoints
**Base URL: `http://localhost:8081`**

## Authorization scheme
The API uses Basic Authentication. All requests must include an `Authorization` header that contains `Basic ` followed by base 64 encoded form of `username:password`.

For instance, if username is "admin" and password is "password" then the auth header should be: `Authorization: Basic YWRtaW46cGFzc3dvcmQ=`.

The project uses Spring Security for managing the security of the application. In the initial setup, two users are pre-configured in the database:

- User: `admin@email.com`, Password: `admin`, Roles: `ADMIN`
- User: `user@email.com`, Password: `user`, Roles: `USER`

Both the users are configured with bcrypt password encoder in the Spring Security configuration.

### Account API

`GET /api/accounts` - Fetch all accounts

* Response: List of all accounts.
  * Example Response: 
```
{[
    {
    "externalId": 1,
    "accountNumber": "GB33BUKB20201555555555",
    "type": "REGULAR",
    "balance": "0.00",
    "currency": "EUR"
    },
    {
    "externalId": 2,
    "accountNumber": "GB33BUKB20201555555556",
    "type": "SAVINGS",
    "balance": "0.00",
    "currency": "EUR"
    }
 ]}
 ```

`GET /api/accounts/{accountId}` - Fetches the details of a specific account.

* Example: `GET /api/accounts/1`
* Response: Details of account 1 including balance.
    * Example Response: 
```
{
      "externalId": 1,
      "accountNumber": "GB33BUKB20201555555555",
      "type": "REGULAR",
      "balance": "0.00",
      "currency": "EUR"
}
```

`POST /api/deposit/{depositAccountId}` - Deposit a specific amount to an account.

* Request body:
    * `amount`: Amount to be deposited. (Number)
* Example: `POST /api/deposits/1`
* Example request body:
```
{ "amount": 1000.00 }
```
* Response: Returns 200 OK on successful deposit with transaction ID in the body: 
* Example Response: 
```
{ "8d02e931-8ee7-4c12-916f-8098d9b1b38d" }
```

`POST /api/withdrawals/{withdrawalAccountId}` - Withdraw a specific amount from an account.

* Request body:
  * `amount`: Amount to be deposited. (Number)
* Example: `POST /api/withdrawals/1`
* Example request body:
```
{ "amount": 1000.00 }
```
* Response: Returns 200 OK on successful withdrawal with transaction ID in the body:
* Example Response:
```
{ "8d02e931-8ee7-4c12-916f-8098d9b1b38d" }
```

`POST /api/transfers` - Initiates a transfer between two accounts.

* Request Parameters:
    * `fromAccountId`: Account number from which money has to be transferred. (Number)
    * `toAccountId`: Account number to which money has to be transferred. (Number)
    * `amount`: Amount to be transferred. (Number)
* Example: `POST /transfer 
* Example request body: 
```
{ "fromAccountId": 1, "toAccountId": 2, "amount": 500.55 }
```
* Response: Returns 200 OK on successful withdrawal with transaction ID in the body:
* Example Response:
```
{ "8d02e931-8ee7-4c12-916f-8098d9b1b38d" }
```

## Installation

*Just clone the repo and run as a standard Spring Boot application.*
  * git clone https://github.com/username/banking-api.git cd banking-api ```./gradlew bootRun```
  * run test: ```./gradlew test```

## How to Use

Once the app is running, you can use the above endpoints through a tool like curl or Postman. For example, to get account details using curl:
```
curl -X GET 'http://localhost:8081/api/accounts/1' --header 'Authorization: Basic YWRtaW5AZW1haWwuY29tOmFkbWlu'
```


* To deposit money into an account:
```
curl -X POST -H "Content-Type: application/json" -d '{"amount":1000}' http://localhost:8081/api/deposits/1 --header 'Authorization: Basic YWRtaW5AZW1haWwuY29tOmFkbWlu'
```

* To withdraw money from an account:
```
curl -X POST -H "Content-Type: application/json" -d '{"amount":1000}' http://localhost:8081/api/withdrawals/1 --header 'Authorization: Basic YWRtaW5AZW1haWwuY29tOmFkbWlu'
```

* To Transfer money from one account to another
  ``` curl -X POST 'http://localhost:8081/api/transfers' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Basic YWRtaW5AZW1haWwuY29tOmFkbWlu' \
    --data '{
    "fromAccountId": 1,
    "toAccountId": 3,
    "amount": 500.50
    }'
  ```


# Nubank - API balances Test

### Tech - Used Plugins/Requires
* [Clojure] - 1.8.0
* [Leiningen] - 2.7.1 
* [Compojure] - 1.5.1
* [Ring] - 0.2.1
* [Clj-time] - 0.14.0
* [Prismatic Schema Validation] - 1.1.7
* [Cloverage] - 1.0.9

### Installation
Clone repo 
```sh
$ git clone ; cd 
```
Install the dependencies and start the server.
```sh
$ 
$ lein deps; lein deps :tree # check if installed correctlly
$ lein ring server
$ lein test # RUN the tests
```
### List all HTTP Status Code used
| RESPONSE | CODE | MEANING
| ------ | ------ | -------
| Success | 200 | get operation response an ok
| Created | 201 | a post request response an ok
| Not Found | 404 | invalid endpoint
| Bad Request | 400 | type or params incorret on post

## API Request/Response Documentation
## Adding an account POST Method
### Request
```sh
curl -X POST http://localhost:3000/create_account -d '{"account":7895}'  --header "Content-type:application/json"
```
### Response 201 Created
```sh
{"bank/fullname":"Nu Pagamentos S.A.",
 "checking-accounts":
    {"7895":
        {"account/name":"Checking Account","account/created":"2017-10-18T14:20:56Z","operations":[]}
    }
}
```
### Response 201 Created
:body An account was created successfully

### Response 400 Bad request 
e.g Value does not match schema: {:number (not (integer? "7895"}


# First Step - adding the operations on that checking account:
## Adding a Deposit POST Method
### Request
```sh
$ curl -X POST http://localhost:3000/operation \
-d '{"account":7895, "type": "Deposit", "desc": "Account Salary", "amount": 7000.00, "schedule_date": "2017-10-01"}' \ 
--header "Content-type:application/json"
```
### Response 201 Created
:body A transaction was created successfully

### Response 400 Bad request 
e.g Value does not match schema: {:number (not (integer? "7895"}

## Adding a Purchase/Withdrawal POST Method
### Request
```sh
$ curl -X POST http://localhost:3000/operation \
-d '{"account":7895, "type": "Purchase", "desc": "Starbucks", "amount": -1000.00, "schedule_date": "2017-10-02"}' \ 
--header "Content-type:application/json"
```

# Second Step - Get the current balance
## Lookup Account number 7895
### Request
```sh
$ curl -X GET http://localhost:3000/balance/7895 --header "Content-type:application/json"
```
### Response 200 Success
```sh
{
  "check-date": "2017-10-18 10:09:21",
  "checking-account": "7895",
  "current-balance": 6000.00
}
```

# Third step - Get the bank statement
## Lookup Account All Operation of account number 7895
```sh
$ curl -X GET http://localhost:3000/operations?account_id=170690&start_date=2017-08-08&end_date=2017-10-26 --header "Content-type:application/json
```
### Response 200 Success
```sh
{
  "check-date": "2017-10-18 12:44:59",
  "operations": {
    "2017-10-02": [
      {
        "operation/description": "Account Salary",
        "operation/amount": 7000,
        "operation/type": "Deposit",
        "operation/balance": 7000,
        "operation/operation-date": "2017-10-18T14:43:34Z",
        "operation/schedule-date": "2017-10-02"
      }
    ],
    "2017-10-04": [
      {
        "operation/description": "Flight Ticket",
        "operation/amount": -1000.0,
        "operation/type": "Purchase",
        "operation/balance": 6000.0,
        "operation/operation-date": "2017-10-18T14:44:15Z",
        "operation/schedule-date": "2017-10-04"
      },
      {
        "operation/description": "ATM",
        "operation/amount": -50.0,
        "operation/type": "Withdrawal",
        "operation/balance": 5950.0,
        "operation/operation-date": "2017-10-18T14:44:52Z",
        "operation/schedule-date": "2017-10-04"
      }
    ]
  }
}
```

# Fourth step - Compute periods of debt.
## Lookup Account All range date negative of account number 7895
```sh
$ curl -X GET http://localhost:3000/negative?account_id=170690&start_date=2017-08-08&end_date=2017-11-26 --header "Content-type:application/json
```
### Account is still negative untill today response
```sh
{
  "check-date": "2017-10-18 12:50:19",
  "checking-account": 170690,
  "period-balance-negative": [
    {
      "principal": -2050.0,
      "start": "2017-10-08"
    }
  ]
}
```

### Account was negative between 2017-10-08 / 2017-10-10
```sh
{
  "check-date": "2017-10-18 12:53:15",
  "checking-account": 170690,
  "period-balance-negative": [
    {
      "principal": -2050.0,
      "start": "2017-10-08",
      "end": "2017-10-10"
    },
    {
      "principal": -2060.0,
      "start": "2017-10-10"
    }
  ]
}
```

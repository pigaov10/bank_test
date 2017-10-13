# Balances - Nubank Test

```sh
$ curl -X GET http://localhost:3000/operations/12345
```
```sh
[
    {
        "operation/description": "ATM withdrawal",
        "operation/amount": 50,
        "operation/type": "withdrawal",
        "operation/purchase-date": "2017-10-13T02:03:51Z"
    },
    {
        "operation/description": "Online Deposit",
        "operation/amount": 1000,
        "operation/type": "deposit",
        "operation/purchase-date": "2017-10-13T02:03:51Z"
    },
    {
        "operation/description": "Digital Ocean Sistemas",
        "operation/amount": 12,
        "operation/type": "purchase",
        "operation/purchase-date": "2017-10-13T02:03:51Z"
    }
]
```

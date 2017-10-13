(ns api-nubank.core
  (:require [api-nubank.account :refer [create-checking-account accounts]]
            [api-nubank.transaction :refer [create-operation]]))

(create-checking-account 12345)
(create-checking-account 56789)

(-> accounts (create-operation 12345 "withdrawal" "ATM withdrawal" 50.00))
(-> accounts (create-operation 12345 "deposit" "Online Deposit" 1000.00))
(-> accounts (create-operation 12345 "purchase" "Digital Ocean Sistemas" 12.00))

(-> accounts (create-operation 56789 "purchase" "Digital Ocean Sistemas" 5.00))

(defn get-last-operations-by-account
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (get-in @accounts [:checking-accounts checking-account-number :operations]))



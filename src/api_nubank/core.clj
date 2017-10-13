(ns api-nubank.core
  (:require [api-nubank.account :refer [create-checking-account accounts]]
            [api-nubank.transaction :refer [create-operation]]))

(def customer 12345)

(create-checking-account customer)

(-> accounts
    (create-operation customer "deposit" "Online Deposit" 1000.00)
    (create-operation customer "withdrawal" "ATM withdrawal" -50.00)
    (create-operation customer "purchase" "Digital Ocean Sistemas" -12.00)
)


(defn get-last-operations-by-account
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (get-in @accounts [:checking-accounts checking-account-number :operations]))



(defn balance
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (let [operations (get-in @accounts [:checking-accounts checking-account-number :operations])]
     (reduce + 0 (map :operation/amount operations))))

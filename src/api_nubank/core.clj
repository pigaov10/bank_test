(ns api-nubank.core
  (:require [api-nubank.account :refer [create-checking-account accounts]]
            [api-nubank.transaction :refer [create-operation]]))

(def customer 12345)

(create-checking-account customer)

(-> accounts
       (create-operation customer "deposit" "Online Deposit" 3000.00)
;;     (create-operation customer "withdrawal" "ATM withdrawal" -50.00)
;;     (create-operation customer "purchase" "Digital Ocean Sistemas" -12.00)
)

accounts

(defn get-last-operations-by-account
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (get-in @accounts [:checking-accounts checking-account-number :operations]))


(defn third_step
  "returns the bank statement of an account given"
  [account-number]
  (let [operation (get-in @accounts [:checking-accounts account-number :operations])]
  (group-by :operation/purchase-date operation)
  (filter
    #(and
         (> (compare (% :operation/purchase-date) "2005-11-14") 0)
         (< (compare (% :operation/purchase-date) "2018-08-03") 0))
    operation)
  ))


(defn balance
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (let [operations (get-in @accounts [:checking-accounts checking-account-number :operations])]
     (reduce + 0 (map :operation/amount operations))))

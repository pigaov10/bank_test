(ns api-nubank.transaction
  (:require [api-nubank.account :refer [create-checking-account accounts]]))


(defn now [] (new java.util.Date))

(defn create-operation
  "Create a new transaction for a specific checking account
  <accounts> Accounts collection
  <checking-account-number> CC Number
  <type> (PURCHASE, DEPOSIT, WITHDRAWAL)
  <description> A short description about operation
  <amount> transaction amount"
  [accounts checking-account-number type description amount date]
  (let [])
  (get-in @accounts [:checking-accounts checking-account-number]
          (swap! accounts
                 update-in [:checking-accounts checking-account-number :operations]
                 conj {:operation/description    description
                       :operation/amount         amount
                       :operation/type           type
                       :operation/balance        (+ amount (reduce + 0
                                                                   (map :operation/amount
                                                                        (get-in @accounts [:checking-accounts
                                                                                           checking-account-number
                                                                                           :operations]))))
                       :operation/operation-date (now)
                       :operation/schedule-date  date})) accounts)


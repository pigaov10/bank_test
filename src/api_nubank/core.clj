(ns api-nubank.core
  (:require [api-nubank.account :refer [create-checking-account accounts]]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [api-nubank.transaction :refer [create-operation]]))


(defn get-last-operations-by-account
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (get-in @accounts [:checking-accounts checking-account-number :operations]))



;; Step One
;; can add an operation to a given checking
;; account, identified by the account number.
;; (create-checking-account customer)

(defn create-operation-given-account
  "Creates a map with the last detail transactions
  <checking-account-number> CC Number
  <checking-account-number> CC Number
  <checking-account-number> CC Number
  <checking-account-number> CC Number"
  [cc-number type desc amount]
  (-> accounts
    (create-operation cc-number type desc amount "2017-08-10"))
      (get-last-operations-by-account cc-number))



;; Step Two - returns the current balance of a given account
;; This balance is the sum of all operations until today

(defn get-current-balance
  "Creates a map with the last detail transactions
  <checking-account-number> CC Number"
  [checking-account-number]
  (let [operations (get-last-operations-by-account checking-account-number)]
    (get (first operations) :operation/balance) ))



;; Step Three
;; returns the bank statement of an account given
;; a period of dates. This statement will contain the operations of each day
;; and the balance at the end of each day

(defn get-bank-statement-given-account
  "returns the bank statement of an account given
  <checking-account-number> CC Number
  <checking-account-number> CC Number
  <checking-account-number> CC Number"
  [account-number start-date end-date]
  (let [operation (get-in @accounts [:checking-accounts account-number :operations])]
  (filter
    #(and
         (> (compare (% :operation/purchase-date) start-date) 0)
         (< (compare (% :operation/purchase-date) end-date) 0))
    operation)
    (->> operation (group-by :operation/purchase-date))
))


;; Step Four
;; returns the periods which the account's balance
;; was negative, i.e,
;; periods when the bank can
;; charge interest on that account

(defn get-bank-statement-given-account
  "returns the periods which the account's balance was negative
  <checking-account-number> CC Number
  <checking-account-number> CC Number
  <checking-account-number> CC Number"
  [account-number start-date end-date]
  (let [operation (get-last-operations-by-account checking-account-number)]
  (filter
    #(and
         (> (compare (% :operation/purchase-date) start-date) 0)
         (< (compare (% :operation/purchase-date) end-date) 0))
    operation)
    (->> operation (group-by :operation/purchase-date))
))

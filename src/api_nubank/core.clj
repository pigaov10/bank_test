(ns api-nubank.core
  (:require [api-nubank.account :refer [create-checking-account accounts]]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [api-nubank.transaction :refer [create-operation]]))



;; (defn create-account
;;   "Create a checking account
;;   <account-number> Checking Account Number"
;;   [account-number]
;;   (create-checking-account account-number))


;; (defn list-accounts [] @accounts)


(defn get-last-operations-by-account
  "Creates a map with the last detail transactions"
  [account-number]
  (get-in @accounts [:checking-accounts account-number :operations]))


;; Step One
;; can add an operation to a given checking
;; account, identified by the account number.
;; (create-checking-account customer)

(defn create-operation-given-account
  "Creates a map with the last detail transactions
  <account-number> Checking Account Number
  <type> Transaction Type (PURCHASE, DEPOSIT, WITHDRAWAL)
  <desc> Transaction Description
  <amount> Transaction Amount
  <idate> Date of transaction"
  [account-number type desc amount idate]
  (-> accounts
    (create-operation account-number type desc amount idate))
      (get-last-operations-by-account account-number))


;; Step Two - returns the current balance of a given account
;; This balance is the sum of all operations until today

(defn get-current-balance
  "Creates a map with the last detail transactions
  <checking-account-number> Checking Account Number"
  [account-number]
  (let [operations (get-last-operations-by-account account-number)]
    (get (last operations) :operation/balance) ))


;; Step Three
;; returns the bank statement of an account given
;; a period of dates. This statement will contain the operations of each day
;; and the balance at the end of each day

(defn get-bank-statement-given-account
  "returns the bank statement of an account given
  <account-number> Checking Account Number
  <start-date> Start Date
  <end-date> End Date"
  [account-number start-date end-date]
  (let [operation (get-last-operations-by-account account-number)]
    (->> operation
      (filter
      #(and
           (>= (compare (% :operation/purchase-date) start-date) 0)
           (<= (compare (% :operation/purchase-date) end-date) 0)))
      (group-by :operation/purchase-date))
))


;; (create-checking-account 12345)

(-> accounts (create-operation 12345 "Purchase" "Uber" -7000.43 "2017-08-08" ))

accounts

;; Step Four
;; returns the periods which the account's balance
;; was negative, i.e,
;; periods when the bank can
;; charge interest on that account

(defn get-period-account-was-balance-negative
  "returns the periods which the account's balance was negative
  <account-number> Checking Account Number
  <start-date> Start negative date
  <end-date> End negative date"
  [account-number start-date end-date]
  (let [operation (get-last-operations-by-account account-number)]
   (->> operation (reduce-kv (fn [mp key value]
                  (if (neg? (get-in operation [key :operation/balance]))
                  (conj mp value)
                  (println (str "It's not negative" value) )))
                  {})
                  (group-by :operation/purchase-date))
))



;; (get-period-account-was-balance-negative 12345 "2017-08-01" "2017-10-10")

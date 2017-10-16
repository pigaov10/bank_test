(ns api-nubank.core
  (:require [api-nubank.account :refer [create-checking-account accounts]]
            [api-nubank.transaction :refer [create-operation]]))


(defn get-last-operations-by-account
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (get-in @accounts [:checking-accounts checking-account-number :operations]))


(defn get-operations-by-range-date
  "returns the bank statement of an account given"
  [account-number start-date end-date]
  (let [operation (get-in @accounts [:checking-accounts account-number :operations])]
  (filter
    #(and
         (> (compare (% :operation/purchase-date) start-date) 0)
         (< (compare (% :operation/purchase-date) end-date) 0))
    operation)
    (->> operation (group-by :operation/purchase-date))
))

;; Step One
;; can add an operation to a given checking
;; account, identified by the account number.
;; (create-checking-account customer)

(defn recieve-http-operation
  "Creates a map with the last detail transactions"
  [cc-number type desc amount]
  (-> accounts
    (create-operation cc-number type desc amount "2017-08-06"))
      (get-last-operations-by-account cc-number))


;; Step Two - returns the current balance of a given account
;; This balance is the sum of all operations until today

(defn balance
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (let [operations (get-in @accounts [:checking-accounts checking-account-number :operations])]
     (reduce + 0 (map :operation/amount operations))))


;; Step Three
;; returns the bank statement of an account given
;; a period of dates. This statement will contain the operations of each day
;; and the balance at the end of each day

(defn return-balance-statement
  [id]
  (reduce-kv (fn [m k v]
    (update-in m [k] conj {:operations v}
                          {:balance 100})) {}
      (-> id (get-operations-by-range-date "2016-01-01" "2017-09-10"))))

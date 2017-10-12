(ns api-nubank.core
  (:require [api-nubank.account :refer [create-checking-account accounts]]
            [api-nubank.transaction :refer [create-operation]]))

(create-checking-account 12345)
(create-checking-account 56789)

(-> accounts (create-operation 12345 "purchase" "Amazon.Com.Br" 87.45))
(-> accounts (create-operation 12345 "purchase" "Uber do Brasil Tecnologia Ltda." 30.03))


(defn get-last-operations-by-account
  "Creates a map with the last detail transactions"
  [checking-account-number]
  (get-in @accounts [:checking-accounts checking-account-number :operations]))



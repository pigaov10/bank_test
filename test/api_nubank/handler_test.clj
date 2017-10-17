(ns api-nubank.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [api-nubank.core :refer :all]
            [api-nubank.account :refer :all]
            [api-nubank.transaction :refer [create-operation]]
            [api-nubank.handler :refer :all]))


(defn setup [f]
  (f)
  (ns-unmap 'user 'accounts))


(use-fixtures :once setup)

(deftest test-create-accounts

  (testing "After create bank collection, accounts must count zero"
      (is (= (count (get accounts :checking-accounts)) 0)))

  (testing "After create an account the keyword accounts must return 1"
    ;; Creating an Account
    (create-checking-account 123456)

      ;; test if brand new account have been created
      (= "Checking Account" (get-in accounts [:checking-accounts 123456 :account/name]))

      ;; operation array of 12345 account must return zero
      (is (= (count (get-in accounts [:checking-accounts 123456 :operations])) 0))))



(deftest test-create-a-transaction-for-given-account

  (testing "Creating an operation for a given account number"

    ;; Testing a Deposit
    (create-operation-given-account 12345 "Test Deposit" "Salary Test" 6000.00 "2017-08-08" )

    ;; test transaction description
    (= "Salary Test" (get-in accounts [:checking-accounts 123456 :operations :operation/description]))

    ;; test transaction type
    (= "Test Deposit" (get-in accounts [:checking-accounts 123456 :operations :operation/type]))

    ;; test transaction amount
    (= 6000.00 (get-in accounts [:checking-accounts 123456 :operations :operation/amount]))

  )
)

(deftest test-calculate-balance-after-purchase-operation

  (testing "Creating an operation for a given account number"
    ;; Testing a Deposit
    (create-operation-given-account 12345 "Test Deposit" "Salary Test" 6000.00 "2017-08-08" )
    (create-operation-given-account 12345 "Test Purchase" "Market Test" -1000.00 "2017-08-08" )
    (is (= (get-current-balance 12345) 5000.00))
  )
)


(deftest test-payload-api-rest

  (testing "Test if lein server is running correctlly"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "API is running"))))

  (testing "An invalid Restful endpoint should return 404 status code"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))
)

(ns api-nubank.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [api-nubank.core :refer :all]
            [api-nubank.account :refer :all]
            [api-nubank.transaction :refer [create-operation]]
            [api-nubank.handler :refer :all]))


(defn setup [f]
  (defonce test_accounts accounts)
  (f)
  (ns-unmap 'user 'test_accounts))


(use-fixtures :once setup)

(deftest test-create-accounts

  (testing "After create bank collection, accounts must count zero"
      (is (= (count (get test_accounts :checking-accounts)) 0)))

  (testing "After create an account the keyword accounts must return 1"
    ;; Creating an Account
    (create-checking-account 123456)

      ;; test if brand new account have been created
      (= "Checking Account" (get-in test_accounts [:checking-accounts 123456 :account/name]))

      ;; operation array of 12345 account must return zero
      (is (= (count (get-in test_accounts [:checking-accounts 123456 :operations])) 0))))



(deftest test-create-a-transaction-for-given-account

  (testing "Creating an operation for a given account number"

    ;; Testing a Deposit
    (create-operation-given-account 123456 "Test Deposit" "Salary Test" 6000.00 "2017-08-08" )

    ;; test transaction description
    (= "Salary Test" (get-in test_accounts [:checking-accounts 123456 :operations :operation/description]))

    ;; test transaction type
    (= "Test Deposit" (get-in test_accounts [:checking-accounts 123456 :operations :operation/type]))

    ;; test transaction amount
    (= 6000.00 (get-in test_accounts [:checking-accounts 123456 :operations :operation/amount]))

  )
)

(deftest test-calculate-balance-after-purchase-operation

  (testing "Creating an operation for a given account number"
    ;; Testing a Deposit
    (create-checking-account 54321)

    (create-operation-given-account 54321 "Test Deposit" "Salary Test" 6000.00 "2017-08-08")
    (create-operation-given-account 54321 "Test Purchase" "Market Test" -1000.00 "2017-08-08")

    (is (= (get-current-balance 54321) 5000.00))
  )
)


(deftest test-if-date-is-negative

  (testing "Creating an operation that turns a balance negative"
    ;; Testing a Deposit
    (create-checking-account 678767)

    ;; making an account balance negative
    (create-operation-given-account 678767 "Test Deposit" "Salary Test" 6000.00 "2017-08-08")
    (create-operation-given-account 678767 "Test Purchase" "A Big Operation" -7000.00 "2017-08-10")

    (let [negative (update-map (get-period-account-was-balance-negative 678767 "2017-08-01" "2017-08-15"))]
      (is (= (->> negative (first) (:principal)) -1000.00)))
  )
)



(deftest test-payload-api-rest

  (testing "An invalid Restful endpoint should return 404 status code"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))
)

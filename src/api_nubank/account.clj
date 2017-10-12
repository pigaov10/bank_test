(ns api-nubank.account)

(defn now [] (new java.util.Date))

(def accounts (atom {:bank/fullname "Nu Pagamentos S.A."
                    :checking-accounts {} }))

(def new-account {:account/name "Checking Account"
                  :account/created (now)
                  :operations [] })


(defn create-checking-account
  "Creates a CC account if not exists
  <checking-account-number> CC Number"
  [checking-account-number]
  (swap! accounts
        update-in [:checking-accounts]
        assoc checking-account-number new-account ))




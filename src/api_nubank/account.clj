(ns api-nubank.account)

(defn now [] (new java.util.Date))

(def accounts (atom {:bank/fullname "Nu Pagamentos S.A."
                     :checking-accounts {} }))


(defn create-checking-account
  "Creates a CC account if not exists
  <checking-account-number> CC Number"
  [checking-account-number]
  (let [account-exists (get-in accounts [:checking-accounts checking-account-number])]
    (if (nil? account-exists)
      (swap! accounts
            update-in [:checking-accounts]
            assoc checking-account-number {:account/name "Checking Account"
                                           :account/created (now)
                                           :operations [] } )
    )
  ))

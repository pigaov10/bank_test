(ns api-nubank.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer :all]
            [compojure.handler :as handler]
            [api-nubank.core :refer :all]
            [ring.middleware.json :as middleware]))


(defroutes app-routes


  (POST "/create_account" request
    (let [number (read-string (get-in request [:body :account]))]
      (response (create-account number))))

  (GET "/list_accounts"
       []
    (response (list-accounts) ))

  (GET "/operations*" {params :query-params}
    (let [cast-id (read-string (get params "account_id"))
          sdate (get params "start_date")
          edate (get params "end_date")]
       (response (get-bank-statement-given-account cast-id sdate edate) )))

  (GET "/balance/:id" [id]
    (let [cast-id (read-string id)]
      (response {:checking-account id
                 :current-balance (get-current-balance cast-id)} )))

  (POST "/operation" request
    (let [number (read-string (get-in request [:body :account]))
          type (get-in request [:body :type])
          desc (get-in request [:body :desc])
          idate (get-in request [:body :transaction_date])
          amount (get-in request [:body :amount])]
      (response (create-operation-given-account number type desc amount idate))))
  (route/resources "/")



  (route/not-found "Not Found"))


(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

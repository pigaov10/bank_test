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


           (GET "/operations*" {params :query-params}
             (let [cast-id (read-string (get params "account_id"))
                   sdate (get params "start_date")
                   edate (get params "end_date")]
               (response {:check-date (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss") (new java.util.Date))
                          :operations (get-bank-statement-given-account cast-id sdate edate)} )))


           (GET "/negative*" {params :query-params}
             (let [cast-id (read-string (get params "account_id"))
                   sdate (get params "start_date")
                   edate (get params "end_date")]
               (response (update-map (get-period-account-was-balance-negative cast-id sdate edate)))))


           (GET "/balance/:id" [id]
             (let [cast-id (read-string id)]
               (response {:check-date (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss") (new java.util.Date))
                          :checking-account id
                          :current-balance  (get-current-balance cast-id)})))

           (POST "/operation" request
             (let [number (read-string (get-in request [:body :account]))
                   type (get-in request [:body :type])
                   desc (get-in request [:body :desc])
                   idate (get-in request [:body :schedule_date])
                   amount (get-in request [:body :amount])]
               (response (create-operation-given-account number type desc amount idate))))
           (route/resources "/")


           (route/not-found "Not Found"))

(defn wrap-schema
  [handler]
  (fn [request]
    (println request)
    (handler request)))


(def app
  (-> (handler/site app-routes)
      ;(wrap-schema)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))

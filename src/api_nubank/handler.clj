(ns api-nubank.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer :all]
            [compojure.handler :as handler]
            [api-nubank.core :refer :all]
            [ring.middleware.json :as middleware]))


(defroutes app-routes

  (GET "/operations/:id" [id]
    (let [cast-id (read-string id)]
      (response (return-balance-statement cast-id) )))


  (GET "/balance/:id" [id]
    (let [cast-id (read-string id)]
      (response {:checking-account id
                 :current-balance (balance cast-id)} )))

  (POST "/operation" request
    (let [number (read-string (get-in request [:body :account]))
          type (get-in request [:body :type])
          desc (get-in request [:body :desc])
          amount (get-in request [:body :amount])]
      (response (recieve-http-operation number type desc amount))))
  (route/resources "/")

  (route/not-found "Not Found"))


(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

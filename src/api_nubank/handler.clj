(ns api-nubank.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [api-nubank.core :refer :all]
            [datomic.api :refer [create-database transact connect delete-database q db entity pull]]
            [ring.middleware.json :as middleware]))

(defroutes app-routes

  (GET "/balance" request
    (let [name "raphael"]
      {:status 200
       :body (get-last-operations-by-account 12345) }))


  (GET "/balance" request
    (let [name "raphael"]
      {:status 200
       :body {:name name}}))



  (GET "/operation" request
    (let [name "raphael"]
      {:status 200
       :body {:name name}}))


  (POST "/teste" request
    (let [name (or (get-in request [:params :name])
                   (get-in request [:body :name])
                   "John Doe")]
      {:status 200
       :body {:name name
       :desc (str "The name you sent to me was " name)}}))
  (route/resources "/")
  (route/not-found "Not Found"))


(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

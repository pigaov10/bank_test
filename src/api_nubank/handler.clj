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
      (response (get-last-operations-by-account cast-id) )))


  (GET "/statement/:id" [id]
    (let [cast-id (read-string id)]
      (response (third_step 12345) )))


  (GET "/balance/:id" [id]
    (let [cast-id (read-string id)]
      (response {:checking-account id
                 :current-balance (balance cast-id)} )))

  (route/not-found "Not Found"))


(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

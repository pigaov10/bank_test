(defproject api-nubank "0.1.0-SNAPSHOT"
  :description "Nubank API"
  :url "http://localhost"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [com.datomic/datomic-free "0.9.5561.62"]
                 [ring/ring-json "0.3.1"]
                 [lein-cloverage "1.0.9"]
                 [clj-time "0.14.0"]
                 [expectations "2.2.0-rc1"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"][lein-autoexpect "1.9.0"]]
  :ring {:handler api-nubank.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})

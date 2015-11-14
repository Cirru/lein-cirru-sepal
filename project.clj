(defproject cirru/lein-sepal "0.0.9"
  :description "Leiningen plugin for compiling Cirru Sepal into Clojure"
  :url "https://github.com/Cirru/lein-cirru-sepal"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[cirru/sepal "0.0.5"]
                 [cirru/parser "0.0.3"]
                 [hawk "0.2.5"]]
  :eval-in-leiningen true
  :cirru-sepal {:paths ["cirru-src" "cirru-test"]
                :extension ".clj"})

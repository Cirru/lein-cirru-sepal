(ns leiningen.cirru-sepal
  (:require [cirru.parser.core :as parser]
            [cirru.sepal :as sepal]
            [clojure.java.io :as io]
            [clojure.string :refer (replace-first)]
            [hawk.core :as hawk]))

(def cwd (str (System/getenv "PWD") "/"))
(def compile-from "source")
(def compile-to "src")

(defn create-path [source-path]
  (replace-first
    (replace-first source-path "cirru-" "")
    ".cirru" ".clj"))

(defn compile-code [code]
  (sepal/make-code (parser/pare code "")))

(defn compile-file [filename]
  (println (str "Compiling: " filename))
  (with-open [wrtr (io/writer (create-path filename))]
    (.write wrtr (compile-code (slurp filename)))))

(defn is-cirru [f]
  (some? (re-matches #".*\.cirru" (.getName f))))

(defn compile-all []
  (println "Start compiling files.")
  (doall (map compile-file (filter is-cirru
    (concat
      (file-seq (io/file "cirru-src/"))
      (file-seq (io/file "cirru-test/")))))))

(defn listen-file [event]
  (if (is-cirru (:file event))
    (let
      [ filename (.getAbsolutePath (:file event))
        relativePath (clojure.string/replace filename cwd "")]
      (compile-file relativePath))))

(defn watch-all []
  (println "Start watching files.")
  (hawk/watch! [{:paths ["cirru-src" "cirru-test"]
                 :handler (fn [context event]
                            (listen-file event)
                            context)}])
  (loop []
    (Thread/sleep 400)
    (recur)))

(defn cirru-sepal [project & args]
  (if (= (first args) "watch")
    (watch-all)
    (compile-all)))

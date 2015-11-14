(ns leiningen.cirru-sepal
  (:require [cirru.parser.core :as parser]
            [cirru.sepal :as sepal]
            [clojure.java.io :as io]
            [clojure.string :refer (replace-first)]
            [hawk.core :as hawk]))

(def cwd (str (System/getenv "PWD") "/"))
(def compile-from "source")
(def compile-to "src")

(defn create-path [source-path extension]
  (replace-first
    (replace-first source-path "cirru-" "")
    ".cirru" extension))

(defn compile-code [code]
  (sepal/make-code (parser/pare code "")))

(defn compile-file [filename extension]
  (println (str "Compiling: " filename))
  (with-open [wrtr (io/writer (create-path filename extension))]
    (.write wrtr (compile-code (slurp filename)))))

(defn is-cirru [f]
  (some? (re-matches #".*\.cirru" (.getName f))))

(defn compile-all [paths extension]
  (println "Start compiling files.")
  (doall (map
    (fn [path] (compile-file path extension))
    (filter is-cirru
      (apply concat
        (map (fn [path] (file-seq (io/file path))) paths))))))

(defn listen-file [event extension]
  (if (is-cirru (:file event))
    (let
      [ filename (.getAbsolutePath (:file event))
        relativePath (clojure.string/replace filename cwd "")]
      (compile-file relativePath extension))))

(defn watch-all [paths extension]
  (println "Start watching files.")
  (hawk/watch! [{:paths paths
                 :handler (fn [context event]
                            (listen-file event extension)
                            context)}])
  (loop []
    (Thread/sleep 400)
    (recur)))

(defn cirru-sepal [project & args]
  (let
      [configurations (:cirru-sepal project)
        paths (:paths configurations)
        extension (:extension configurations)]
    (if (= (first args) "watch")
      (watch-all paths extension)
      (compile-all paths extension))))

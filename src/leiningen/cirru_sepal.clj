(ns leiningen.cirru-sepal
  (:require [cirru.parser.core :as parser]
            [cirru.sepal :as sepal]
            [clojure.java.io :as io]
            [clojure.string :refer (replace-first)]
            [hawk.core :as hawk])
  (:import (java.io File)))

(def cwd (str (System/getenv "PWD") "/"))
(def compile-from "source")
(def compile-to "src")

(defn replace-extension [source-path]
  (replace-first source-path ".cirru" ""))

(defn replace-filename [source-path]
  (-> source-path
    (replace-first "cirru-" "")
    (replace-first ".cirru" "")))

(defn compile-code [code]
  (sepal/make-code (parser/pare code "")))

(defn compile-file [filename]
  (println (str "Compiling: " filename))
  (let
    [result (compile-code (slurp filename))]
    (with-open [wrtr (io/writer (replace-extension filename))]
      (.write wrtr result))
    (io/make-parents (replace-filename filename))
    (.renameTo
      (File. (replace-extension filename))
      (File. (replace-filename filename)))))

(defn is-cirru [f]
  (some? (re-matches #".*\.cirru" (.getName f))))

(defn compile-all [paths]
  (println "Start compiling files.")
  (doall (map
    (fn [path] (compile-file path))
    (filter is-cirru
      (apply concat
        (map (fn [path] (file-seq (io/file path))) paths))))))

(defn listen-file [event]
  (if (is-cirru (:file event))
    (let
      [ filename (.getAbsolutePath (:file event))
        relativePath (clojure.string/replace filename cwd "")]
      (compile-file relativePath))))

(defn watch-all [paths]
  (println "Start watching files.")
  (hawk/watch! [{:paths paths
                 :handler (fn [context event]
                            (listen-file event)
                            context)}])
  (loop []
    (Thread/sleep 400)
    (recur)))

(defn cirru-sepal [project & args]
  (let
      [configurations (:cirru-sepal project)
        paths (:paths configurations)]
    (if (= (first args) "watch")
      (watch-all paths)
      (compile-all paths))))

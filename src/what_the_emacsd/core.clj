(ns what-the-emacsd.core
  (:require [ring.middleware.content-type :refer [wrap-content-type]]
            [stasis.core :as stasis]
            [optimus.assets :as assets]
            [optimus.prime :as optimus]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :refer [serve-live-assets]]
            [optimus.export]
            [what-the-emacsd.posts :as posts]
            [what-the-emacsd.pages :as pages]))

(defn get-assets []
  (concat
   (assets/load-bundle "public" "/styles.css" ["/styles/reset.css"
                                               "/styles/main.css"])
   (assets/load-assets "public" ["/favicon.ico"
                                 "/images/logo.png"
                                 #"/scripts/.*\.js"])))

(defn load-posts []
  (posts/get-posts (stasis/slurp-directory "resources/posts/" #"\.html$")))

(defn get-pages []
  (require 'what-the-emacsd.templates :reload)
  (pages/get-pages (load-posts)))

(def optimize optimizations/all)

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(def export-directory "./build/")

(defn- load-export-dir []
  (stasis/slurp-directory export-directory #"\.[^.]+$"))

(defn export []
  (let [assets (optimize (get-assets) {})
        old-files (load-export-dir)]
    (stasis/empty-directory! export-directory)
    (optimus.export/save-assets assets export-directory)
    (stasis/export-pages (get-pages) export-directory {:optimus-assets assets})
    (println)
    (println "Export complete:")
    (stasis/report-differences old-files (load-export-dir))
    (println)))

(ns what-the-emacsd.pages
  (:require [what-the-emacsd.templates :as tmpl]
            [what-the-emacsd.rss :as rss]))

(defn- create-page [f]
  (fn [context] (apply str (tmpl/layout context (f)))))

(defn- single-posts [posts]
  (->> posts
       (partition-all 2 1)
       (map (fn [[post next-post]]
              [(:path post)
               (create-page #(tmpl/one-post post next-post))]))
       (into {})))

(defn get-pages [posts]
  (merge
   {"/index.html" (create-page #(tmpl/all-posts posts))
    "/atom.xml" (rss/atom-xml posts)}
   (single-posts posts)))

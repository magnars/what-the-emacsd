(ns what-the-emacsd.pages
  (:require [what-the-emacsd.templates :as tmpl]
            [clj-time.coerce]))

(defn- create-page [f]
  (fn [context] (apply str (tmpl/layout context (f)))))

(defn- extract-date [content]
  (-> (re-find #"^<!-- (\d+) -->" content) second
      (str "000") Long/parseLong clj-time.coerce/from-long))

(defn- to-post [[path content]]
  {:path path
   :content content
   :date (extract-date content)
   :name (subs path 1 (- (count path) 5))}) ; chop off / and .html

(defn- single-posts [posts]
  (->> posts
       (partition-all 2 1)
       (map (fn [[post next-post]]
              [(:path post)
               (create-page #(tmpl/one-post post next-post))]))
       (into {})))

(defn- get-posts [content]
  (->> content :posts (map to-post) (sort-by :date) reverse))

(defn get-pages [content]
  (let [posts (get-posts content)]
    (merge
     {"/index.html" (create-page #(tmpl/all-posts posts))}
     (single-posts posts))))

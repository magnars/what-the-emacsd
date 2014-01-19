(ns what-the-emacsd.posts
  (:require [clj-time.coerce]))

(defn- extract-date [content]
  (-> (re-find #"^<!-- (\d+) -->" content) second
      (str "000") Long/parseLong clj-time.coerce/from-long))

(defn- to-post [[path content]]
  {:path path
   :content content
   :date (extract-date content)
   :name (subs path 1 (- (count path) 5))}) ; chop off / and .html

(defn get-posts [content]
  (->> content :posts (map to-post) (sort-by :date) reverse))

(ns what-the-emacsd.rss
  (:require [clojure.data.xml :as xml]))

(defn- entry [post]
  [:entry
   [:title (:name post)]
   [:updated (:date post)]
   [:author [:name "Magnar Sveen"]]
   [:link {:href (str "http://whattheemacsd.com" (:path post))}]
   [:id (str "urn:whattheemacsd-com:feed:post:" (:name post))]
   [:content {:type "html"} (:content post)]])

(defn atom-xml [posts]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:id "urn:whattheemacsd-com:feed"]
     [:updated (-> posts first :date)]
     [:title {:type "text"} "What the .emacs.d!?"]
     [:link {:rel "self" :href "http://whattheemacsd.com/atom.xml"}]
     (map entry posts)])))

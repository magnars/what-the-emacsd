(defproject what-the-emacsd "0.1.0"
  :description "The webpage for whattheemacsd.com"
  :url "http://whattheemacsd.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [stasis "2.2.2"]
                 [enlive "1.1.5"]
                 [optimus "0.14.2"]
                 [ring "1.2.1"]
                 [clj-time "0.5.1"]
                 [org.clojure/data.xml "0.0.7"]]
  :ring {:handler what-the-emacsd.core/app
         :port 3456}
  :aliases {"build-site" ["run" "-m" "what-the-emacsd.core/export"]}
  :profiles {:dev {:plugins [[lein-ring "0.8.7"]]
                   :source-paths ["dev"]}})

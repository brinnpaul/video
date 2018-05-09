(defproject video "0.1.0-SNAPSHOT"
  :description "Video Streaming Server"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [compojure "1.1.6"]]
   :plugins [[lein-ring "0.8.10"]]

   ; See https://github.com/weavejester/lein-ring#web-server-options for the
   ; various options available for the lein-ring plugin
   :ring {:handler video.handler/app
          :nrepl {:start? true
                  :port 9998}}
   :profiles
   {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                         [ring-mock "0.1.5"]]}
     :uberjar {:aot :all}}
  :main video.core
  :target-path "target/%s")

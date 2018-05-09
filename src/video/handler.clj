(ns video.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]))
(require '[ring.util.response :as resp])
(require '[ring.util.request :as req])
(require '[ring.middleware.params :refer [wrap-params]])
(require '[clojure.string :as str])
(require '(clojure.java [io :as io]))

; Import functionaity from `video` module
(use 'video.video)

(def videoFilePath "{filepath/to/video}")
(def videoSize (get_file_size videoFilePath))

; Parse Byte Range HTTP Request Headers
(defn getRangerHeader [req]
  (let [headers (get req :headers)]
    (get headers "range")))

(defn get_start [range]
  (read-string (first range)))

(defn get_end [range]
  (if (> (count range) 1)
      (read-string (last range))
    nil))

; Return appropriate byte info of file based on HTTP Request headers
(def stream
  (GET "/stream" req
      (let [range (getRangerHeader req)
            byteRange (str/split (str/replace range #"bytes=" "") #"-")
            start (get_start byteRange)
            end (get_end byteRange)]
      (try
        (if (nil? end)
          (let [resp (-> (resp/response (get_some_bytes videoFilePath 0 videoSize))
                         (resp/status 200)
                         (resp/header "Content-Length" (str (- videoSize 1)))
                         (resp/header "Connection" "keep-alive")
                         (resp/content-type "video/mp4"))]
                         resp)
          (let [resp (-> (resp/response (get_some_bytes videoFilePath start end))
                         (resp/status 206)
                         (resp/header "Content-Range" (str "bytes " start "-" end " /" videoSize))
                         (resp/header "Accept-Ranges" "bytes")
                         (resp/header "Content-Length" (+ (- end start) 1))
                         (resp/header "Connection" "keep-alive")
                         (resp/content-type "video/mp4"))]
                         resp))
         (catch Exception e (str "caught exception: " (.getMessage e))))
         )))

; Route wrapper -> display byte range requested
(defn wrap_byte_range_request [handler]
  (fn [req]
    (let [range (getRangerHeader req)]
         (println "Requested Byte Range: " range))
    (handler req)))

(defroutes app-routes
  (GET "/" [] "!!!")
  (GET "/heartbeat" [] "!!!")
  (GET "/video" [] (slurp (io/resource "public/video.html")))
  (wrap_byte_range_request (wrap-params stream)))

(def app app-routes)

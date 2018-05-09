(ns video.video
  (:import (java.io File BufferedInputStream
                    FileInputStream InputStreamReader InputStream
                    FileOutputStream OutputStreamWriter OutputStream
                    ByteArrayInputStream ByteArrayOutputStream)))

(require '(clojure.java [io :as io]))

; Output file as byte array
; @Input String
; @Output [Byte]
(defn file_to_bytes [file]
  (with-open [xin (io/input-stream file)
              xout (java.io.ByteArrayOutputStream.)]
    (io/copy xin xout)
    (.toByteArray xout)))

; Display byte array as text
; @Input [Byte]
; @Output String
(defn bytes_to_string [bytes]
  (apply str (map char bytes)))

; Get File Size
; @Input String
; @Output int
(defn get_file_size [filename]
  (.length (io/file filename)))

; Copies specified bytes from file into file buffer
; @Input String int int
; @Output ByteArrayInputStream
(defn get_some_bytes [filename skip readamount]
  (let [f (FileInputStream. filename)
        buffer (byte-array readamount)]
    (if (not (nil? skip))
      (.skip f skip))
    (if (nil? readamount)
      (.read f buffer)
      (.read f buffer 0 readamount))
    (ByteArrayInputStream. buffer)))

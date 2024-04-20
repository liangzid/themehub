(ns color-set-backend.core
  (:gen-class))

(use 'clojure.string)

(defrecord ColorRGB [r g b hex])

(defn hex-to-decimal [hex-str]
  (Integer/parseInt (replace hex-str "#" "") 16)
  )

(defn from-hex [hex-str]
  (->ColorRGB
   (hex-to-decimal (subs hex-str 1 3))
   (hex-to-decimal (subs hex-str 3 5))
   (hex-to-decimal (subs hex-str 5 7))
   hex-str
   )
  )

(def theme-transformer
     {"blue"  "#C2E8F7",
     "red"  "#FCE0E1",
     "yellow"  "#F2F4C1",
     "orange"  "#FFE2BB",
     "green"  "#CCE7CF",
     "grey-dark"  "#DBDFEF",
     "grey-white"  "#F3F3F4",
     "purple"  "#C5BEDF"}
  )



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))




(ns themes.colorblock
  (:require [rum.core :as rum]))




(defonce copied-color (atom {:color "#FFFFF"}))


;; failed due to the clipboard paste.
(defn copy-to-clipboard [text]
  (let [temp-el (js/document.createElement "textarea")
        set-value! (.-innerText temp-el)
        add-listener (fn [el event-listener]
                       (.adEvenetListener el "blur"
                                          event-listener))
        ]
    (set! (.-innerText temp-el) text)
    (.add js/document.body temp-el)
    (add-listener temp-el #(.remove js/document temp-el))
    (.select temp-el)
    (.execCommand js/document "copy")
    )
  )



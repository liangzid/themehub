(ns themes.app
  (:require [com.biffweb :as biff :refer [q]]
            [themes.middleware :as mid] ;; middlewares
            ;; define page, base, and error page, too complex.
            [themes.ui :as ui]
            ;; name used in other place
            [themes.settings :as settings]
            [rum.core :as rum] ;; html render
            [xtdb.api :as xt] ;; xtdb
            [ring.adapter.jetty9 :as jetty]
            [cheshire.core :as cheshire]))

(defn message [{:msg/keys [text sent-at]}]
  [:.mt-3 {:_ "init send newMessage to #message-header"}
   [:.text-gray-600 (biff/format-date sent-at "dd MMM yyyy HH:mm:ss")]
   [:div text]])

(defn notify-clients [{:keys [themes/chat-clients]} tx]
  (doseq [[op & args] (::xt/tx-ops tx)
          :when (= op ::xt/put)
          :let [[doc] args]
          :when (contains? doc :msg/text)
          :let [html (rum/render-static-markup
                      [:div#messages {:hx-swap-oob "afterbegin"}
                       (message doc)])]
          ws @chat-clients]
    (jetty/send! ws html)))

(defn send-message [{:keys [session] :as ctx} {:keys [text]}]
  (let [{:keys [text]} (cheshire/parse-string text true)]
    (biff/submit-tx ctx
      [{:db/doc-type :msg
        :msg/user (:uid session)
        :msg/text text
        :msg/sent-at :db/now}])))

(defn chat [{:keys [biff/db]}]
  (let [messages (q db
                    '{:find (pull msg [*])
                      :in [t0]
                      :where [[msg :msg/sent-at t]
                              [(<= t0 t)]]}
                    (biff/add-seconds (java.util.Date.) (* -60 10)))]
    [:div {:hx-ext "ws" :ws-connect "/app/chat"}
     [:form.mb-0 {:ws-send true
                  :_ "on submit set value of #message to ''"}
      [:label.block {:for "message"} "Write a message"]
      [:.h-1]
      [:textarea.w-full#message {:name "text"}]
      [:.h-1]
      [:.text-sm.text-gray-600
       "Sign in with an incognito window to have a conversation with yourself."]
      [:.h-2]
      [:div [:button.btn {:type "submit"} "Send message"]]]
     [:.h-6]
     [:div#message-header
      {:_ "on newMessage put 'Messages sent in the past 10 minutes:' into me"}
      (if (empty? messages)
        "No messages yet."
        "Messages sent in the past 10 minutes:")]
     [:div#messages
      (map message (sort-by :msg/sent-at #(compare %2 %1) messages))]]))

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

;; (doseq [[k v] theme-transformer]
;;      (println k v))

(rum/defc a-color-card [color-name color-hex]
  [:div {:style {:width "50px"
                 :height "40px"
                 :background-color color-hex}}
])

(rum/defc a-theme-card [theme-dict theme-name]
  [:div
   [:p.text-2xl.font-mono.text-center theme-name]
   [:div.gap-4.place-content-center
     (for [[color hex] theme-dict]
       (a-color-card color hex)
       )
   ]])

(defn app [{:keys [session biff/db] :as ctx}]
  (let [{:user/keys [email foo bar]} (xt/entity db (:uid session))]
    (ui/page
     {}
     ;; information of sign in and sign out.
     [:div.font-serif.right-0
      "Signed in as " email ". "
      (biff/form
       {:action "/auth/signout"
        :class "inline"}
       [:button.bg-black.rounded-lg.shadow-xl.text-white.hover:text-blue-800
        {:type "submit"
         }
        " Sign out "])
      ]
     ;; seperating with whitespaces.
     [:.h-20]

     ;;--- color theme here we set.
     [:h1.text-3xl.font-bold "Theme Gallery"]
     [:.h-10]

     (a-theme-card theme-transformer "Transformer")

     [:label ""]

     ;;--- color theme ends here.
     [:.h-20]

     [:.h-6]
     (chat ctx)
     )))

(defn ws-handler [{:keys [themes/chat-clients] :as ctx}]
  {:status 101
   :headers {"upgrade" "websocket"
             "connection" "upgrade"}
   :ws {:on-connect (fn [ws]
                      (swap! chat-clients conj ws))
        :on-text (fn [ws text-message]
                   (send-message ctx {:ws ws :text text-message}))
        :on-close (fn [ws status-code reason]
                    (swap! chat-clients disj ws))}})

(def about-page
  (ui/page
   {:base/title (str "About " settings/app-name)}
   [:p "This app was made with "
    [:a.link {:href "https://biffweb.com"} "Biff"] "."]
   ))

(defn echo [{:keys [params]}]
  {:status 200
   :headers {"content-type" "application/json"}
   :body params})

(def module
  {:static {"/about/" about-page}
   :routes ["/app" {:middleware [mid/wrap-signed-in]}
            ["" {:get app}]
            ["/set-foo" {:post set-foo}]
            ["/set-bar" {:post set-bar}]
            ["/chat" {:get ws-handler}]]
   :api-routes [["/api/echo" {:post echo}]]
   :on-tx notify-clients})

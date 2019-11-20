(ns wtf.views
  (:require
   [re-frame.core :as re-frame]
   [wtf.subs :as subs]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Heello froem " @name]
     [:button {:on-click #(re-frame/dispatch [:refresh-app-infos] )}]
     ]))

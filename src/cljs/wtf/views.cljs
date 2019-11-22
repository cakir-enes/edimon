(ns wtf.views
  (:require
   [re-frame.core :as re-frame]
   [wtf.subs :as subs]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        appz (re-frame/subscribe [:app-infos])]
    [:div
     [:h1 "Heello froem " @appz]
     [:button {:on-click #(re-frame/dispatch [:refresh-app-infos] )}]]))

; (defn app-info-panel []
;   (let [app-info {:name "ESM" :params []}]
;     [:div
;      []]))

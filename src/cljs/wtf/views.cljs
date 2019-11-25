(ns wtf.views
  (:require
   [re-frame.core :as rf]
   [wtf.subs :as subs]
   [goog.functions :as gf]
   [reagent.core :as r]))

(def status-map {:FTE :checked})
(defn map-to-html-list [m]
  (into [:ul]
        (for [[k v] m]
          (when-not (empty? v) [:li k (map-to-html-list v)]))))

(defn atom-input [value]
  (let [temp (reagent.core/atom "")
        deb-changer (gf/debounce #(reset! value @temp) 400)]
    
    [:input {:type "text"
             :value @value
             :on-change #(reset! value (-> % .-target .-value))}]))


(defn search-box []
  (let [query (rf/subscribe [::subs/query])]
    [:p "Search..." 
     [:input {:type "text" 
              :value @query 
              :on-change #(rf/dispatch [:query-changed (-> % .-target .-value)])}]]))

(defn main-panel []
  (let [appz (rf/subscribe [::subs/visible-paths])
        vec->list (fn [paths] [:ul (for [path paths] ^{:key path} [:li path])])]
  
    [:div
     [search-box]
     [:ul
      (for [[name paths] @appz]
        ^{:key name} [:li name [vec->list paths]])]]))


; (defn app-info-panel []
;   (let [app-info {:name "ESM" :params []}]
;     [:div
;      []]))

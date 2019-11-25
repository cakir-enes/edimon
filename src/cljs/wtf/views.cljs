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
    [:div  {:class "bg-red" :style {:text-align "center"}}
     
     [:input {:type "text" 
              :value @query 
              :on-change #(rf/dispatch [:query-changed (-> % .-target .-value)])}]]))

(defn main-panel []
  (let [appz (rf/subscribe [::subs/visible-paths])
        checks {}
        vec->list (fn [paths]
                    [:ul (for [path paths]
                           (do (assoc checks path (r/atom false))
                               ^{:key path}
                              [:div
                                    [:input {:type "checkbox"
                                             :checked (get checks path)
                                             :on-click #(update checks swap! not)}]
                                    [:label path]]))])]
    
    [:div {:class "bg-gold sans-serif center"}
     [search-box]
     [:ul {:class "mw9 center ph3-ns"
           :style {:display "grid"
                   :grid-template-columns "1fr 1fr"
                   :grid-gap "10px"}}
      (for [[name paths] @appz]
        ^{:key name} [:li {:style {:display "inline-block" :color "#fff" :background-color "#111"}} name [vec->list paths]])]]))


; (defn app-info-panel []
;   (let [app-info {:name "ESM" :params []}]
;     [:div
;      []]))

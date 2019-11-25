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
    [:div.w-80  {:class "card center"}
     [:input {:type "text" 
              :value @query 
              :on-change #(rf/dispatch [:query-changed (-> % .-target .-value)])}]]))


(defn parameter []
  (let [new-val (r/atom "")]
    (fn [{:keys [path val type]}]
      [:div {:style {:display "grid" :grid-template-columns "3fr 1fr 1fr"}}
       [:h4 path]
       [:h4 val]
       [:h4 type]])))
(def black "#292826")
(def yellow "#f9d342")
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
                                    [:label.mh3 path]]))])]
    [:link {:rel "stylesheet" :href "/Users/ecakir/Projects/wtf/resources/public/index.css"}]
    [:div {:style {:background-color "#292826"}}
     [search-box]
     [:ul.cards {:style {:display "grid"
                         :grid-template-columns "1fr 1fr"
                         :grid-gap "10px"}}
      (for [[name paths] @appz]
        ^{:key name} [:li {:class "card code"
                           :style {:display "inline-block" :color yellow :background-color "#292826"}}
                      [:span.ph3 {:style {:background-color yellow
                                      :color "black"}} name]
                      [vec->list paths]])]]))




; (defn app-info-panel []
;   (let [app-info {:name "ESM" :params []}]
;     [:div
;      []]))

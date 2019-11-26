(ns wtf.views
  (:require
   [re-frame.core :as rf]
   [wtf.subs :as subs]
   [goog.functions :as gf]
   [reagent.core :as r]))

(def black "#292826")
(def yellow "#f9d342")

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

(defn- filter-selected [app-paths]
  (reduce-kv
   (fn [m app-name paths] (assoc m app-name (keep (fn [[path check?]] (when check? path)) paths)))
   {}
   app-paths))

(defn selection-panel []
  (let [appz (rf/subscribe [::subs/visible-paths])
        checks (r/atom {})
        vec->list (fn [app-name paths]
                    [:ul (for [path paths]
                           ^{:key (str app-name path)}
                           [:div
                            [:input {:type "checkbox"
                                     :for path
                                     :checked (get-in @checks [app-name path])
                                     :on-change #(swap! checks update-in [app-name path] not)
                                     :style {:vertical-align "2px"}}]
                            [:label.mh3 {:id path} path]])])]
    [:div {:style {:background-color "#292826"}}
     [search-box]
     [:button {:on-click #(rf/dispatch [:update-selected-paths (filter-selected @checks)])} "WATCH"]
     [:ul.cards {:style {:display "grid"
                         :grid-template-columns "1fr 1fr"
                         :grid-gap "10px"}}
      (for [[name paths] @appz]
        ^{:key name} [:li {:class "card code"
                           :style {:display "inline-block" :color yellow :background-color "#292826"}}
                      [:span.ph3 {:style {:background-color yellow
                                          :color "black"}} name]
                      [vec->list name paths]])]]))

(defonce subz (r/atom {}))

(def click-count (r/atom {}))

(defn counting-component []
  [:ul 
   (doall
    (for [c [:c1 :c2 :c3]]
      (do
        (swap! click-count assoc c (r/atom 0))
        ^{:key c}
        [:div
         "The atom " [:code "click-count " c] " has value: "
         @(get @click-count c) ". "
         [:input {:type "button" :value "Click me!"
                  :on-click #(swap! (get @click-count c) inc)}]])))])

(defn subs-panel []
  (let [params (rf/subscribe [::subs/subbed-params])
        prepend (fn [app-name param] (update param :path #(str app-name "." %)))]
    [:div 
     [counting-component]
     [:ul
      (doall 
        (for [{:keys [path type]} (reduce-kv (fn [v app-name params] (concat v (map #(prepend app-name %) params))) [] @params)] 
          (do 
            (swap! subz assoc path (r/atom "valval"))
            ^{:key path}
            [:div {:style {:display "grid" :grid-template-columns "3fr 1fr 1fr"}}
             [:h4 path]
             [:h4 @(get @subz path)]
             [:h4 type]])))]]))

(defn main-panel []
  [:link {:rel "stylesheet" :href "/Users/ecakir/Projects/wtf/resources/public/index.css"}]
  [:div 
   [selection-panel]
   [subs-panel]])




; (defn app-info-panel []
;   (let [app-info {:name "ESM" :params []}]
;     [:div
;      []]))

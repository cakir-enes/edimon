(ns wtf.subs
  (:require
   [re-frame.core :as re-frame]))


(defn find-and-update [tree sub-paths]
  (update-in tree (butlast sub-paths) (fnil #(conj % (last sub-paths)) [])))

(defn to-tree [paths]
  (reduce (fn [tree sub-paths]
            (find-and-update tree sub-paths))
          {}
          (map #(clojure.string/split % ".") paths)))

(defn map-to-html-list [m]
  (into [:ul]
        (for [[k v] m]
          [:li k
           (if (map? v)
             (map-to-html-list v)
             (if (not (empty? v)) (conj [:ul] (map #(vector :li %) v)) nil))])))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :app-infos
 (fn [db]
   (let [app-name-to-paths (reduce (fn [app-infos [app-name info]]
                                     (assoc app-infos app-name (map :path (:params info))))
                                   {}
                                   (:app-infos db))]
     (reduce (fn [tree [app-name paths]]
               (assoc tree (name app-name) (to-tree paths)))
             app-name-to-paths))))

(re-frame/reg-sub
 :selected-paths
 (fn [db]
   (:selected-paths db)))

(re-frame/reg-sub
 :subbed-params
 (fn [db]
   (:subbed-params db)))
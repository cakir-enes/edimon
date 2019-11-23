(ns wtf.subs
  (:require
   [re-frame.core :as re-frame]
   [wtf.trie :as trie]))

(defn map-to-html-list [m]
  (into [:ul]
        (for [[k v] m]
          (when-not (empty? v) [:li (str k) (map-to-html-list v)]))))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(defn app-infos->trie [app-infos]
  (->>
   (reduce (fn [m [app-name info]] (assoc m app-name (map :path (:params info)))) {} app-infos)
    (map (fn [[app-name paths]] {app-name (trie/build paths)}))
   (reduce merge)))

(re-frame/reg-sub
 :app-infos
 (fn [db]
   (app-infos->trie (:app-infos db))))

(re-frame/reg-sub
 :selected-paths
 (fn [db]
   (:selected-paths db)))

(re-frame/reg-sub
 :subbed-params
 (fn [db]
   (:subbed-params db)))
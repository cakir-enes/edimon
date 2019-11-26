(ns wtf.subs
  (:require
   [re-frame.core :as re-frame]
   [wtf.trie :as trie]))


(defn toggle! [node]
 (swap! (:status node) not)
  ())



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
 ::app-infos
 (fn [db]
   (reduce (fn [m [app-name info]] 
             (assoc m app-name (map :path (:params info)))) 
           {} 
           (:app-infos db))))

; (re-frame/reg-sub
;  :app-infos
;  (fn [db]
;    (app-infos->trie (:app-infos db))))

(re-frame/reg-sub
 ::selected-paths
 (fn [db]
   (:selected-paths db)))

(re-frame/reg-sub
 ::subbed-params
 (fn [db]
   (->>
    (:selected-paths db)
    (reduce-kv (fn [m app-name paths]
                 (let [params (reduce (fn [m param] (assoc m (:path param) param)) {} (get-in db [:app-infos app-name :params]))]
                   (assoc m app-name (map #(get params %) paths))))
               {}))))

(re-frame/reg-sub
 ::query
 (fn [db]
   (:query db)))

(re-frame/reg-sub
 ::visible-paths
 (fn [db]
   (reduce-kv (fn [m k v] 
                (assoc m k 
                       (filter #(clojure.string/includes? % (:query db)) (map :path (:params v)))))
              {}
              (:app-infos db))))


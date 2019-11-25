(ns wtf.events
  (:require
   [re-frame.core :as rf]
   [wtf.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 :refresh-app-infos
 (fn [db _]))

(rf/reg-event-db
 :update-selected-paths
 (fn [db [_ new-paths]]
   (assoc db :selected-paths new-paths)))

(rf/reg-event-db
 :update-vals
 (fn [db [_ new-vals]]
   (assoc db :subbed-params new-vals)))

(rf/reg-event-db
 :set-val
 (fn [db [_ [path new-val]]]
   (assoc-in db [:subbed-params (keyword path)] new-val)))

(rf/reg-event-db
 :query-changed
 (fn [db [_ new-query]]
   (assoc db :query new-query)))



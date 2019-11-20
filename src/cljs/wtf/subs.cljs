(ns wtf.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :app-infos
 (fn [db]
   (:app-infos db)))

(re-frame/reg-sub
 :selected-paths
 (fn [db]
   (:selected-paths db)))

(re-frame/reg-sub
 :subbed-params
 (fn [db]
   (:subbed-params db)))

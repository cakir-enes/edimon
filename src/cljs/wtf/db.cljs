(ns wtf.db)

(def default-db
  {:name "re-framee"
   :app-infos {:FTE {:params [{:path "THIS.IS.STR" :type "STRING"} {:path "THIS.IS.ENUM" :type "ENUM"}]
                     :enums {"THIS.IS.ENUM" ["A", "B"]}}
               :ESM {:params [{:path "THIS.IS.INT" :type "INT"}]
                     :enums {}}}
   :selected-paths []
   :subbed-params {}
   :query ""})

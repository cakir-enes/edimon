(ns wtf.trie)

(defn add [trie x]
  (let [breadcumbs (clojure.string/split x ".")]
    (assoc-in trie breadcumbs (merge (get-in trie breadcumbs) {x {}}))))

(defn in? [trie x]
  "Returns true if the value x exists in the specified trie."
  (not (nil? (get-in trie (clojure.string/split x ".")))))

(defn prefix-matches [trie prefix]
  "Returns a list of matches with the prefix specified in the trie specified."
  (keep key (tree-seq map? vals (get-in trie (clojure.string/split prefix ".")))))

(defn build [coll]
  "Builds a trie over the values in the specified seq coll."
  (reduce add {} coll))

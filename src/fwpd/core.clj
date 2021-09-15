(ns fwpd.core)

(def filename "suspects.csv")

(slurp filename)

(def vampire-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(str->int "12")

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vampire-keys value]
  ((get conversions vampire-keys) value))

(convert :glitter-index 3)

(defn parse
  "Convert a CSV file into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name Renan :glitter-index 0}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vampire-keys unmapped-row)))
       rows))

(first (mapify (parse (slurp filename))))

(defn glitter-filter
  [minimun-glitter records]
  (filter #(>= (:glitter-index %) minimun-glitter) records))

(glitter-filter 3 (mapify (parse (slurp filename))))

(defn get-names
  [list-of-maps]
  (map :name (into [] list-of-maps)))

(get-names (glitter-filter 3 (mapify (parse (slurp filename)))))

(def validations [:name :glitter-index])

(defn valid-append?
  [vampire-keys validations]
  (every? vampire-keys validations))


(defn append-suspects
  [suspects new-suspects]
  (if (valid-append? new-suspects validations)
    (into suspects new-suspects))
  print "argumentos invalidos")

(append-suspects (into [] (mapify (parse (slurp filename)))) [{:name "Renan" :glitter-index 0}])





(validate {:name "Renan" :glitter-index 3} validations)

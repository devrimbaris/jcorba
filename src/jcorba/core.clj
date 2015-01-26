(ns jcorba.core
  (:import [org.jsoup Jsoup]
           [org.jsoup.nodes
            Attribute Attributes Comment DataNode
            Document Element TextNode]))



;(html/parse "http://devrimbaris.github.io")

(defn get-mp3-url [word]
  (let [url  (str  "http://dictionary.cambridge.org/dictionary/british/" word)
        mp3-url (if-let [doc (try  (.get (Jsoup/connect url)) (catch Exception e1 nil))]
                  (let [mp3-link-elements (.select doc "span.us")
                        mp3-urls (->> mp3-link-elements
                                      (map #(.attr % "data-src-mp3"))
                                      (remove empty?)
                                      distinct)]
                    (first  mp3-urls)))]
    mp3-url))

(get-mp3-url "grandma")





















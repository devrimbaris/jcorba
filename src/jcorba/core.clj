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


(defn get-aa-teaser []
  (let [url  (str  "http://www.aa.com.tr/tr" )
        working-set (if-let [doc (try  (.get (Jsoup/connect url)) (catch Exception e1 nil))]
                      (let [teasers  (.select doc "ol.teasers li h3 a")]
                        teasers
                        ))]
                                        
    (map #(.attr (.select % "a") "href") working-set)
    ))


(get-aa-teaser)




;(.getElementsByTag (second   (.getAllElements (first mp3-url))) "a")
                                        ;(.select (second   (.getAllElements (first mp3-url))) "a")

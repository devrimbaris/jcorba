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


(defn get-aa-teasers []
  (let [url  (str  "http://www.aa.com.tr/tr/" )
        working-set (if-let [doc (try  (.get (Jsoup/connect url)) (catch Exception e1 nil))]
                      (let [teasers  (.select doc "ol.teasers li h3 a")]
                        teasers
                        ))]
                                        
    (map #(assoc {}
            :url (str url (.attr (.select % "a") "href"))
            :title (.html (.select % "a") )
            :description ""
            ) working-set)
    ))

(get-aa-teasers)


(defn get-cihan-teasers []
  (let [url  (str  "http://www.cihan.com.tr/home/" )
        working-set (if-let [doc (try  (.get (.userAgent  (Jsoup/connect url) "Mozilla"))
                                       (catch org.jsoup.HttpStatusException e1  (.getStatusCode e1)))]
                      (.select  doc "#last-news li"))]
    working-set
    (map #(assoc {}
            :url (str url (.attr (.select % "a") "href"))
            :title (.attr (.select % "a") "title")
            
            ) working-set)
    ))

(get-cihan-teasers)


(defn get-iha-rss []
  (let [url  (str  "http://www.iha.com.tr/rss.php")
        working-set (if-let [doc (try  (.get (Jsoup/connect url) ) (catch org.jsoup.HttpStatusException e1  (.getStatusCode e1)))]
                      doc)
        items (.select working-set "link")]
    (map #(assoc {}
            :url (.html (.select % "link")) 
            :title (.html (.select % "title") )
            :description (.html (.select % "description") )
            :category (.html (.select % "category") )
            ) items)
    items
    
    ))


(defn get-iha-rss []
  (let [url  (str  "http://www.iha.com.tr/rss.php")
        working-set (if-let [doc (try  (.get (Jsoup/connect url)) (catch org.jsoup.HttpStatusException e1  (.getStatusCode e1)))]
                      doc)
        items (.select working-set "item")]
    (map #(assoc {}
            :url (.select % ":contains(tehdit)") 
            :title (.html (.select % "title") )
            :description (.html (.select % "description") )
            :category (.html (.select % "category") )
            ) items)
    working-set
    ))


(get-iha-rss)





;#last-news ul.last-news-list



;(.getElementsByTag (second   (.getAllElements (first mp3-url))) "a")
                                        ;(.select (second   (.getAllElements (first mp3-url))) "a")

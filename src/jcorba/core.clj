(ns jcorba.core
  (:import [org.jsoup Jsoup]
           [java.lang String]
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
            :source "aa"
            :url (str url (.attr (.select % "a") "href"))
            :title (.html (.select % "a") )
            :description ""
            ) working-set)
    ))

(defn get-aa-rss []
  (let [url  (str  "http://www.aa.com.tr/rss/ajansguncel.xml")
        working-set (if-let [doc (try  (.get (Jsoup/connect url) ) (catch org.jsoup.HttpStatusException e1  (.getStatusCode e1)))]
                      doc)
        items (.select working-set "item")]
    (map #(assoc {}
            :source "aa"
            :url (.html (.select % "link") )
            :title (.html (.select % "title") )
            :description (.html (.select % "description") )
            :category (.html (.select % "category") )
            ) items)
    ))


(defn get-cihan-teasers []
  (let [url  (str  "http://www.cihan.com.tr/home/" )
        working-set (if-let [doc (try  (.get (.userAgent  (Jsoup/connect url) "Mozilla"))
                                       (catch org.jsoup.HttpStatusException e1  (.getStatusCode e1)))]
                      (.select  doc "#last-news li"))]
    working-set
    (map #(assoc {}
            :source "cihan"
            :url (str url (.attr (.select % "a") "href"))
            :title (.attr (.select % "a") "title")
            
            ) working-set)
    ))




(defn get-iha-rss []
  (let [url  (str  "http://www.iha.com.tr/rss.php")
        working-set (if-let [doc (try  (.get (Jsoup/connect url) ) (catch org.jsoup.HttpStatusException e1  (.getStatusCode e1)))]
                      doc)
        items (.select working-set "item")]
    (map #(assoc {}
            :source "iha"
            :title (second (re-find #"\[CDATA\[(.*)!\]]"  (.html (.select % "title") )))
            :description (.html (.select % "description") )
            :category (.html (.select % "category") )
            ) items)
    ))



(defn fetch-all-news []
  (concat  (get-cihan-teasers) (get-aa-rss) (get-iha-rss)))


(map #(select-keys % [:title :description]) (fetch-all-news))

(remove nil? (map #(if (re-find #"(?i)borsa" (:title %)) (:title %))

                  (fetch-all-news)))


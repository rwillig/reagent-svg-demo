(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs            "0.0-2814-0"         :scope "test"]
                 [adzerk/boot-cljs-repl       "0.1.9"              :scope "test"]
                 [adzerk/boot-reload          "0.2.4"              :scope "test"]
                 [tailrecursion/boot-hoplon   "0.1.0-SNAPSHOT"     :scope "test"] 
                 [pandeiro/boot-http          "0.6.1"              :scope "test"]
                 [cljsjs/jquery-ui            "1.11.3-1"]
                 [tailrecursion/hoplon        "6.0.0-SNAPSHOT"]])

(require
 '[adzerk.boot-cljs            :refer [cljs]]
 '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload          :refer [reload]]
 '[tailrecursion.boot-hoplon   :refer :all]
 '[pandeiro.boot-http          :refer [serve]])

(deftask build []
  (comp (speak)
        (hoplon)
        (reload)
        (cljs)))

(deftask run []
  (comp (serve :port 3333)
        (watch)
        (cljs-repl)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced })
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none
                       :unified-mode true
                       :source-map true}
                 hoplon {:pretty-print true}
                ;reload {:on-jsload 'svg.app/init}
                 )
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))

(deftask prod
  "Prod build"
  []
  (comp (production)
        (build)))

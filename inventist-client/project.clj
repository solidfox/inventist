(defproject inventist-client "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}



  :min-lein-version "2.7.1"

  :dependencies [[binaryage/oops "0.6.1"]
                 [camel-snake-kebab "0.4.0"]
                 [cljs-react-material-ui "0.2.48"]
                 ;[cljsjs/material-ui "1.2.1-0"]
                 ;[org.clojure/core.async  "0.4.474"]
                 [org.clojars.osbert/clj-time "0.9.2"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [district0x/graphql-query "1.0.5"]
                 [finja "0.1.0-SNAPSHOT"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.roman01la/cljss "1.6.2"]
                 [remodular "0.1.6-SNAPSHOT"]
                 [rum "0.11.2"]
                 [ysera "1.2.0"]
                 [com.cognitect/transit-cljs "0.8.256"]]

  :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
            [lein-figwheel "0.5.15"]]

  :source-paths ["src"]

  :cljsbuild {:builds
              {:dev
               {:source-paths ["src"]

                ;; The presence of a :figwheel configuration here
                ;; will cause figwheel to inject the figwheel client
                ;; into your build
                :figwheel     {:on-jsload "inventist-client.main/on-js-reload"}

                :compiler     {:main                 inventist-client.main
                               :asset-path           "/js/compiled/out"
                               :output-to            "resources/public/js/compiled/inventist_client.js"
                               :output-dir           "resources/public/js/compiled/out"
                               :source-map-timestamp true
                               :preloads             [devtools.preload]}}
               ;:install-deps true
               ;:npm-deps {:react-firebaseui "1.2.3"
               ;           :firebase "4.12.1"}}}
               ;; This next build is a compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once min
               :min
               {:source-paths ["src"]
                :compiler     {:output-to     "resources/public/js/compiled/inventist_client.js"
                               :main          inventist-client.main
                               :optimizations :advanced
                               :pretty-print  false}}}}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs       ["resources/public/css"]       ;; watch and update CSS

             :ring-handler   server.router/handle-request
             :server-logfile "/tmp/figwheel-log.txt"
  ;; Start an nREPL server into the running figwheel process
  ;; :nrepl-port 7888

  ;; Server Ring Handler (optional)
  ;; if you want to embed a ring handler into the figwheel http-kit
  ;; server, this is for simple ring servers, if this

  ;; doesn't work for you just run your own server :) (see lein-ring)

  ;; :ring-handler hello_world.server/handler

  ;; To be able to open files in your editor from the heads up display
  ;; you will need to put a script on your path.
  ;; that script will have to take a file path and a line number
  ;; ie. in  ~/bin/myfile-opener
  ;; #! /bin/sh
  ;; emacsclient -n +$2 $1
  ;;
             :open-file-command "open-in-intellij"}

  ;; if you are using emacsclient you can just use
  ;; :open-file-command "emacsclient"

  ;; if you want to disable the REPL
  ;; :repl false

  ;; to configure a different figwheel logfile path
  ;; :server-logfile "tmp/logs/figwheel-logfile.log"

  ;; to pipe all the output to the repl
  ;; :server-logfile false



  ;; Setting up nREPL for Figwheel and ClojureScript dev
  ;; Please see:
  ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
  :profiles {:dev            {:dependencies  [[binaryage/devtools "0.9.9"]
                                              [figwheel-sidecar "0.5.15"]
                                              [com.cemerick/piggieback "0.2.2"]]
                              ;; need to add dev source path here to get user.clj loaded
                              :source-paths  ["src" "dev"]
                              ;; for CIDER
                              ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                              :repl-options  {:port             8230
                                              :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                              ;; need to add the compliled assets to the :clean-targets
                              :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                                :target-path]}
             :figwheel-nrepl {:repl-options {:init (do (require 'user)
                                                       (user/fig-start))}}})

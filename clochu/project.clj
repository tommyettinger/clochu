(defproject rogue.like/kolonize "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.squidpony/squidlib "1.95.1"]
                 [com.github.tommyettinger/cuttlebone "1.96.1"]]
  :aot :all
  :main rogue.like.kolonize
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

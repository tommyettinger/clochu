(defproject rogue.like/kolonize "0.1.0-SNAPSHOT"
  :description "Dungeon colonization SRPG/Roguelike"
  :url "https://github.com/tommyettinger/kolonize"
  :license {:name "Apache License, version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.squidpony/squidlib "1.95.1"]
                 [com.github.tommyettinger/cuttlebone "1.97.1"]]
  :aot :all
  :main rogue.like.kolonize
  :target-path "target/%s"
  )

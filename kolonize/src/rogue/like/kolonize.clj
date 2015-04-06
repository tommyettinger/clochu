(ns rogue.like.kolonize
  (:require [clojure.java.io :as io]
            [rogue.like.state :as state])
  (:import [squid.squidgrid.map.styled DungeonGen TilesetType]
           [squid.squidgrid.map DungeonUtility]
           [squidpony.squidcolor SColor SColorFactory]
           [squidpony.squidgrid.gui SGPane]
           [squidpony.squidgrid.gui.awt.event SGKeyListener SGKeyListener$CaptureType ]
           [squidpony.squidgrid.fov TranslucenceWrapperFOV BasicRadiusStrategy]
           [squidpony.squidgrid.gui.swing SwingPane]
           [java.awt Font]
           [java.awt.event KeyListener KeyEvent]
           [java.io File]
           [javax.swing JLabel JPanel JFrame JTextField]
           [javax.swing.event DocumentListener])
  (:gen-class))
(set! *warn-on-reflection* true)

(def wide 80)
(def high 40)
(def dg (DungeonGen.))
(def dungeon (DungeonGen/wallWrap
               (.generate ^DungeonGen dg TilesetType/DEFAULT_DUNGEON (int wide) (int high))))
(def vdun (mapv vec dungeon))
(def linear (DungeonUtility/hashesToLines dungeon))

(defonce ^SwingPane sp
  (try (SwingPane. 10 22 wide high
                   (.deriveFont (Font/createFont Font/TRUETYPE_FONT 
                                                 (io/file (io/resource "Rogue-Zodiac.ttf"))) 32.0)) ;
    (catch Exception e (SwingPane. wide high
                                   (Font. Font/MONOSPACED Font/PLAIN 14.0)))))
(defn random-floor [d] (loop [x (rand-int (inc (- wide 2))) y (rand-int (inc (- high 2)))]
                         (if (= (get-in d [x y]) \.)
                           [x y]
                           (recur (rand-int (inc (- wide 2))) (rand-int (inc (- high 2)))))))
(defn initialize []
  (state/put :dungeon vdun)
  (state/put :player {:pos (random-floor vdun) :show \@})
  (state/store "Player Enters"))

(defn -main
  "Get kolonizing that dungeon!"
  [& args]
  (initialize)
  (.placeText sp 0 0 linear SColor/BLACK SColor/LIGHT_GRAY)
  (let [[x y] (:pos (state/lkv :player))]
    (.placeCharacter sp x y \@ SColor/CRIMSON))
  (.refresh sp)
  (doto (JFrame. "Kolonize")
      (.setContentPane sp)
      (.pack)
      (.setVisible true)))

(-main)
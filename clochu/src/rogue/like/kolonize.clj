(ns rogue.like.kolonize
  (:require [clojure.java.io :as io])
  (:import [squid.squidgrid.map.styled DungeonGen TilesetType]
           [squidpony.squidcolor SColor SColorFactory]
           [squidpony.squidgrid.gui SGPane]
           [squidpony.squidgrid.gui.awt.event SGKeyListener SGKeyListener$CaptureType ]
           [squidpony.squidgrid.fov TranslucenceWrapperFOV BasicRadiusStrategy]
           [squidpony.squidgrid.gui.swing SwingPane]
           [java.awt Font Component GridBagLayout GridBagConstraints Insets]
           [java.awt.event KeyListener KeyEvent]
           [javax.swing JOptionPane]
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

(defonce ^SwingPane sp 
  (try (SwingPane. 12 24 wide high
                   (.deriveFont (Font/createFont Font/TRUETYPE_FONT 
                                                 (io/file (io/resource "Rogue-Zodiac.ttf"))) 32.0)) ;
    (catch Exception e (SwingPane. wide high
                                   (Font. Font/MONOSPACED Font/PLAIN 14.0)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (.placeText sp 0 0 dungeon SColor/WHITE SColor/BLACK)
  (.refresh sp)
  (doto (JFrame. "Kolonize")
      (.setContentPane sp)
      (.pack)
      (.setVisible true)))

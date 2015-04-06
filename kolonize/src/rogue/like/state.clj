(ns rogue.like.state)

(def versions (atom {0 {:rollback nil}}))

(def named-versions (atom {"The Beginning" 0}))

(def head (atom 0))

(def staging (atom {:rollback head}))

(defn planned "Gets the current staging state." [] @staging)

(defn scrap-plan "Resets the staging state to the latest non-staging state."
  [] (reset! staging (@versions @head)))

(defn sv "Gets the Staged Value for a given name k." [k] (get @staging k nil))

(defn put "Puts a value v into the staging state with name k." [k v] (swap! staging assoc k v))

(defn lkv "Gets the Last Known Value with name k from the latest non-staging state."
  [k] (get (@versions @head) k nil))

(defn store
  ("Stores the current staging state permanently. Returns the new version number."
   []
   (swap! staging assoc :rollback @head)
   (swap! head inc)
   (swap! versions assoc @head @staging)
    @head)
  ("Stores the current staging state permanently and makes it available with the given naming.
  Naming should not be an integer, but any other key will work. Returns the new version number."
    [naming]
   (swap! staging assoc :rollback @head)
   (swap! head inc)
   (swap! versions assoc @head @staging)
   (swap! named-versions assoc naming @head)
    @head))

(defn restore
  ("Change the state to a copy of the previous one used. Increments the current version,
  does not destroy any stored states, and completely overwrites the staging state with the
  contents of the restored state, except that the staging state will consider the state
  that was current before this call to be its previous state."
   []
   (when ((@versions @head) :rollback)
     (swap! versions assoc (inc @head) (@versions (lkv :rollback)))
     (reset! staging (assoc (@versions (inc @head)) :rollback @head))
     (swap! head inc)
     (swap! versions assoc-in [@head :rollback] (dec @head))))
  ("Change the state to a copy of an earlier version, either by an integer version or a name
  if the version was made available with a (non-integer) naming. Increments the current version,
  does not destroy any stored states, and completely overwrites the staging state with the
  contents of the restored state, except that the staging state will consider the state
  that was current before this call to be its previous state."
    [version]
   (if (integer? version)
     (when ((@versions version) :rollback)
       (swap! versions assoc (inc @head) (@versions version))
       (reset! staging (assoc (@versions (inc @head)) :rollback @head))
       (swap! head inc)
       (swap! versions assoc-in [@head :rollback] (dec @head)))
     (when (and (contains? @named-versions version)
                ((@versions (@named-versions version)) :rollback))
       (swap! versions assoc (inc @head) (@versions (@named-versions version)))
       (reset! staging (assoc (@versions (inc @head)) :rollback @head))
       (swap! head inc)
       (swap! versions assoc-in [@head :rollback] (dec @head))))))





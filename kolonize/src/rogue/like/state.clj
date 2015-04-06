(ns rogue.like.state)

(def versions (atom {0 {:rollback nil}}))

(def named-versions (atom {"The Beginning" 0}))

(def head (atom 0))

(def staging (atom {:rollback head}))

(defn planned "Gets the current staging state." [] @staging)

(defn latest "Gets the latest non-staging state." [] (@versions @head))

(defn scrap-plan "Resets the staging state to the latest non-staging state."
  [] (reset! staging (@versions @head)))

(defn sv "Gets the Staged Value for a given name k." [k] (get @staging k nil))

(defn put "Puts a value v into the staging state with name k." [k v] (swap! staging assoc k v))

(defn modify "Takes the value in the staging state with name k, calls f with that value as its only arg,
  and changes the value in the staging state to the result of f."
  [k f]
  (swap! staging assoc k (f (@staging k))))

(defn lkv "Gets the Last Known Value with name k from the latest non-staging state."
  [k] (get (@versions @head) k nil))

(defn store
  "Stores the current staging state permanently and makes it available with the given naming, if given
  one.
  Naming should not be an integer, but any other key will work. Returns the new version number."
  ([]
   (swap! staging assoc :rollback @head)
   (swap! head inc)
   (swap! versions assoc @head @staging)
    @head)
  ([naming]
   (swap! staging assoc :rollback @head)
   (swap! head inc)
   (swap! versions assoc @head @staging)
   (swap! named-versions assoc naming @head)
    @head))

(defn restore
  "Change the state to a copy of an earlier version, which can be specified by an integer version, a
  name if the version was made available with a (non-integer) naming, or not specified, which will
  revert to the previous one used. Increments the current version, does not destroy any stored states,
  and completely overwrites the staging state with the contents of the restored state, except that the
  staging state will consider the state that was current before this call to be its previous state."
  ([]
   (when ((@versions @head) :rollback)
     (swap! versions assoc (inc @head) (@versions (lkv :rollback)))
     (reset! staging (assoc (@versions (inc @head)) :rollback @head))
     (swap! head inc)
     (swap! versions assoc-in [@head :rollback] (dec @head))))
  ([version]
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





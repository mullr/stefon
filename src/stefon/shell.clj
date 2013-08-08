(ns stefon.shell
  (:require [lamina.core :as lamina]
            [stefon.domain :as domain]
            [stefon.shell.functions :as functions]
            [stefon.shell.plugin :as plugin]
            [stefon.shell.kernel :as kernel]))


(defn create-system
  "Creates a map with the core components of the system kernel"
  []

  {:posts []
   :assets []
   :tags []

   :channel-spout nil
   :channel-sink nil})

(defn start-system
  #_([] (start-system (create-system)))
  #_([system] (start-system system kernel/handle-incoming-messages))
  #_([system kernel-handler]

     ;; Delegate to the kernel
     (kernel/start-system system kernel-handler)

     ;; switch namespaces
     (in-ns 'stefon.shell)

     ;; return *SYSTEM*
     (kernel/get-system)))

(defn stop-system []
  (swap! (kernel/get-system) (fn [inp]  nil))
  (in-ns 'user))


;; SUBSCRIPTION code
(defn close-plugin-channel []
  (plugin/close-plugin-channel (kernel/get-system)))

(defn attach-plugin [receive-handler]
  (plugin/attach-plugin (kernel/get-system) receive-handler))

(defn- publish-event [^clojure.lang.PersistentHashMap event]
  (plugin/publish-event (kernel/get-system) event))

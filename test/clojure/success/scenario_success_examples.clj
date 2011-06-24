(ns success.scenario_success_examples
  (:use expectations.scenarios))

(defn foo [] (println "hi"))
(defn bar [] (foo))

(defn foo2 [a b] (println a b))
(defn bar2 [a b] (foo2 (* a a) (* b b)))

(scenario
 (let [a (atom 0)]
   (swap! a inc)
   (expect 1 @a)
   (swap! a inc)
   (expect 2 @a)))

(scenario
 (expect {:a :b} (in {:a :b :c :d})))

(scenario
 (given [x y] (expect x y)
        1 1
        3 3))

(scenario
  (let [a (atom 1)]
    (given (swap! a inc)
      (expect
        identity 2
        identity 2))))

(scenario
  (binding [bar2 no-op]
    (expect nil? (bar2 3 4))))

(scenario
 (stubbing [foo 1
            bar nil]
           (expect 1 (foo))
           (expect nil? (bar))))

;; success interaction tests
(scenario
 (bar)
 (expect (interaction (foo)) :once))

(scenario
 (expect (interaction (foo)) :never))

(scenario
 (bar2 1 2)
 (expect (interaction (foo2 1 4)) :once))

(scenario
 (bar2 1 2)
 (bar2 1 2)
 (expect (interaction (foo2 1 4)) :twice))

(scenario
 (bar2 1 2)
 (expect (interaction (foo2 anything 4)) :once))

(scenario
 (bar2 1 2)
 (bar2 2 2)
 (expect (interaction (foo2 1 4)) :once))
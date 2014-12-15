(ns whats-my-salary.core)

;;; A calculator to show how much money you will take home when employed as a
;;; perminant employee

;;; Standard tax, NI and allowance rates taken from
;;; http://www.hmrc.gov.uk/rates/it.htm
;;; and http://www.hmrc.gov.uk/rates/nic.htm
;;;
;;; Decimal numbers have been used to prevent lazy evaluation on all percentage
;;; figures due to the way we calculate percentage.
;;;
;; Starting to refactor data into a few small maps, by joining related data together
;; Aiming toward a single, well constructed data structure to manage all data.

;; Another refactoring would be to put functions into some of the data structures
;; for any common calulations that would otherwise be put into more than one function


(def working-times
  "Average working times for a full-time employee,
   useful to compare earnings to that of a contractor"
  {:weeks-in-a-year 47 :days-in-a-week 5 :hours-in-a-day 8})

; Example of accessing a value of a maps key/value pair
(get working-times :weeks-in-a-year)
(working-times :days-in-a-week)
(:hours-in-a-day working-times)


(def income-bands
  "The income levels used to calcuate tax at different rates"
  {:basic   1000.0
   :high   34370.0
   :top   150000.0
   :income-limit 100000} )

(def tax-rate-percent
  "The percentage rate used to calculate tax for difference tax bands"
  {:basic 20.0
   :high 40.0
   :top 45.0} )

(def national-insurance {:minimum-weekly-salary 142.0
                         :maximum-weekly-salary 817.0
                         :percentage 12.0})


(defn earnings-after-basic-tax-rate [monies]
  (- monies (* monies (/ (tax-rate-percent :basic) 100.0))) )

(/ (tax-rate-percent :basic) 100.0)
(* 20000 (/ (tax-rate-percent :basic) 100.0))
(- 20000 (* 20000 (/ (tax-rate-percent :basic) 100.0)) )
(earnings-after-basic-tax-rate 20000)
;;; End of experimental code


(defn whats-my-tax-bands [gross-salary]
  "Which taxation bands do I incur due to salary.  If below the personal tax allowance, then only NI.  If below 37k then only 20%.  If more then everything over 38k earnt taxed at 40%"
  (if (> gross-salary (income-bands :top) )
    (str "You are a fat cat"))
  )

(whats-my-tax-bands 200000)

;; Refactor to combine similar functions,
;; either using paramter overloading or let statements

(defn national-insurance-rate-employed-minimum [monies]
  (* national-insurance-employed-minimum-weekly-salary weeks-in-a-year))

(defn national-insurance-rate-employed-maximum [monies]
  (* national-insurance-employed-maximum-weekly-salary weeks-in-a-year))


(defn taxable-salary [my-salary]
  (- my-salary personal-earnings-allowance))

(defn national-insurance-due [monies]
  (* (taxable-salary monies) (/ national-insurance-percentage 100)))


(defn income-tax-due [monies]
  (* (taxable-salary monies) (/ :basic 100)))


(defn whats-my-yearly-takehome [salary]
  (- salary (income-tax-due salary) (national-insurance-due salary)))

(defn whats-my-monthly-takehome [monies]
  (/ (whats-my-yearly-takehome monies) 12.0))


(whats-my-yearly-takehome 28000)
(whats-my-monthly-takehome 28000)

(whats-my-yearly-takehome 85000)



;;; Main only used for Uber jar - a bit pointless otherwise it would seem

(defn -main
  "I don't do a whole lot.  Used to call the code from the Uberjar on the command line"
  [& args]
  (println "Hello, World!")
  (whats-my-yearly-takehome 28000)
  )

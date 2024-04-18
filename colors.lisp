(ql:quickload '#:com.inuoe.jzon)
(ql:quickload '#:uiop)
(add-package-local-nickname '#:jzon '#:com.inuoe.jzon)


(defstruct Color
  r
  g
  b
  hex)

(defun hex-to-decimal (x)
  (parse-integer (if (and (>= (length x) 2) (equal (subseq x 0 1) "#"))
		 (subseq x 1)
		 x)
		 :radix 16)
  )

(defun new-Color-from-hex (hex)
  (make-color
   :r (hex-to-decimal (subseq hex 1 3))
   :g (hex-to-decimal (subseq hex 3 5))
   :b (hex-to-decimal (subseq hex 5 7))
   :hex hex
   )
  )

(defvar theme-transformer (make-hash-table :test 'equal))

(defvar color-hex-ls
      #(
     ("blue"  "#C2E8F7")
     ("red"  "#FCE0E1")
     ("yellow"  "#F2F4C1")
     ("orange"  "#FFE2BB")
     ("orange"  "#FFE2BB")
     ("green"  "#CCE7CF")
     ("grey-dark"  "#DBDFEF")
     ("grey-white"  "#F3F3F4")
     ("purple"  "#C5BEDF")
        )
      )

(loop for pair across color-hex-ls do
  (progn
  (let ((acolor (nth 0 pair))
	(ahex (nth 1 pair))
	)
    (print acolor)
    (print ahex)
    (setf (gethash acolor theme-transformer) (new-color-from-hex ahex)))
  ))

(jzon:stringify theme-transformer
		:pretty t
		:stream (pathname "./color-themes.json")
		)

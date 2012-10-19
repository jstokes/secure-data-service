(ns genAssessments.edfi_xml
  (:use genAssessments.defines)
  (:use clojure.data.xml)
  (:use clojure.contrib.math)
  (:use [clojure.contrib.string :only (substring?)])  
)

(defn gen-edfi-xml
  [interchange output-file contents]
  (with-open [out (java.io.OutputStreamWriter. (java.io.FileOutputStream. output-file) "UTF-8")]
    (emit (element interchange {:xmlns "http://ed-fi.org/0100"} contents) out)
  )
)

(defn student-ref [schoolName studentId]
  (element :StudentReference {}
    (element :StudentIdentity {}
      (element :StudentUniqueStateId {} (student-id schoolName studentId))
    )
  )
)

(defn assessment-ref [assessmentName]
  (element :AssessmentReference {}
    (element :AssessmentIdentity {}
      (element :AssessmentIdentificationCode {:IdentificationSystem "Test Contractor"}
        (element :ID {} assessmentName)
      )
    )
  )
)  

(defn school-ref [schoolName]
  (element :SchoolReference {}
    (element :EducationalOrgIdentity {}
      (element :StateOrganizationId {} schoolName)
    )
  )
)

(defn ed-org-ref [edOrgName]
  (element :EducationOrganizationReference {}
    (element :EducationalOrgIdentity {} 
      (element :StateOrganizationId {} edOrgName)
    )
  )
)

(defn session-ref [districtName]
  (element :SessionReference {}
    (element :SessionIdentity {}
      (element :SessionName {} (session-id districtName))
    )
  )
)  

(defn section-ref [schoolName]
  (element :SectionReference {}
    (element :SectionIdentity {}
      (element :StateOrganizationId {} schoolName)
      (element :UniqueSectionCode {}  (section-id schoolName))
    )
  )
)

(defn course-ref [districtName]
  (element :CourseReference {}
    (element :CourseIdentity {}
      (element :CourseCode {:IdentificationSystem "CSSC course code"}
        (element :ID {} (course-id districtName))
      )
    )
  )
)

(defn course-offering-ref [districtName schoolName]
  (element :CourseOfferingReference {}
    (element :CourseOfferingIdentity {}
      (element :LocalCourseCode {} (local-course-id districtName schoolName))
      (element :Term {} "Fall Semester")
      (element :SchoolYear {} "2011-2012")
    )
  )
)  

(defn create-student-edfi [schoolName studentId]
  (element :Student {:id (student-id schoolName studentId)}
    (element :StudentUniqueStateId {} (student-id schoolName studentId))
    (element :Name {}
      (element :FirstName {} (rand-nth first-names))
      (element :LastSurname {} (rand-nth last-names))
    )
    (element :Sex {} (rand-nth ["Male" "Female"]))
    (element :BirthData {}
      (element :BirthDate {} (str "2001-" (format "%02d" (inc (rand-int 12))) "-" (format "%02d" (inc (rand-int 28)))))
    )
    (element :HispanicLatinoEthnicity {} (rand-nth ["true" "false"]))
    (element :Race {})
  )
)

(defn create-state-edfi []
  (let [tmp (element :StateEducationAgency {:id "NY"}
    (element :StateOrganizationId {} "NY")
    (element :NameOfInstitution {} "New York State Board of Education")
    (element :OrganizationCategories {}
      (element :OrganizationCategory {} "State Education Agency")
    )
    (element :Address {}
      (element :StreetNumberName {} "123 Street")
      (element :City {} "Albany")
      (element :StateAbbreviation {} "NY")
      (element :PostalCode {} "11011")
    )
  )]
  tmp
  )
)

(defn create-district-edfi [districtName]
  (let [tmp (element :LocalEducationAgency {:id districtName}
    (element :StateOrganizationId {} districtName)
    (element :NameOfInstitution {} districtName)
    (element :OrganizationCategories {}
      (element :OrganizationCategory {} "Local Education Agency")
    )
    (element :Address {}
      (element :StreetNumberName {} "123 Street")
      (element :City {}  districtName)
      (element :StateAbbreviation {} "NY")
      (element :PostalCode {} "11011")
    )
    (element :LEACategory {} "Independent")
    (element :StateEducationAgencyReference {} 
      (element :EducationalOrgIdentity {}
        (element :StateOrganizationId {} "NY"))))]
    tmp
  )
)

(defn create-school-edfi [districtName, schoolName]
  (let [tmp (element :School {:id schoolName}
    (element :StateOrganizationId {} schoolName)
    (element :EducationOrgIdentificationCode {:IdentificationSystem "School"}
      (element :ID {} schoolName)
    )
    (element :NameOfInstitution {} schoolName)
    (element :OrganizationCategories {}
      (element :OrganizationCategory {} "School")
    )
    (element :Address {:AddressType "Physical"}
      (element :StreetNumberName {} "123 Street")
      (element :City {} "Albany")
      (element :StateAbbreviation {} "NY")
      (element :PostalCode {} "11011")
      (element :NameOfCounty {} districtName)
    )
    (element :Telephone {:InstitutionTelephoneNumberType "Main"}
      (element :TelephoneNumber {} "(425)-555-1212" )
    )
    (element :GradesOffered {}
      (element :GradeLevel {} "First grade")
      (element :GradeLevel {} "Second grade")
      (element :GradeLevel {} "Third grade")
      (element :GradeLevel {} "Fourth grade")
      (element :GradeLevel {} "Fifth grade")
      (element :GradeLevel {} "Sixth grade")
    )
    (element :SchoolCategories {}
      (element :SchoolCategory {} "Elementary School")
    )
    (element :LocalEducationAgencyReference {:ref districtName})
  )]
  tmp
  )
)

(defn create-course-edfi [districtName]
  (let [tmp (element :Course {:id (course-id districtName)}
    (element :CourseTitle {} "Math 7")
    (element :NumberOfParts {} "1")
    (element :CourseCode {:IdentificationSystem "CSSC course code"}
        (element :ID {} (course-id districtName))
    )
    (element :CourseDescription {} "7th grade math")
    (ed-org-ref districtName)
    (element :UniqueCourseId {} (course-id  districtName)))]
    tmp
  )
)

(defn create-student-assessment-association-edfi [schoolName studentId assessmentName date]
  (let [score (rand-nth (range 6 33))]
    (element :StudentAssessment {:id (saa-id schoolName studentId date)}
      (element :AdministrationDate {} date)
      (element :AdministrationEndDate {} date)
      (element :SerialNumber {} "2")
      (element :ScoreResults {:AssessmentReportingMethod "Scale score"}
        (element :Result {} (str score))
      )
      (element :PerformanceLevels {}
        (element :CodeValue {} (performance-level score))
      )
      (student-ref schoolName studentId)
      (assessment-ref assessmentName)
    )
  )
)

(defn create-student-school-association-edfi [schoolName studentId]
  (element :StudentSchoolAssociation {}
    (student-ref schoolName studentId)
    (school-ref schoolName)
    (element :EntryDate {} "2011-09-01")
    (element :EntryGradeLevel {} "Seventh grade")
  )
)

(defn create-student-section-association-edfi [schoolName studentId]
  (element :StudentSectionAssociation {}
    (student-ref schoolName studentId)
    (section-ref schoolName)
    (element :BeginDate {} "2011-09-06")
    (element :EndDate {} "2011-12-16")
  )
)

(defn create-assessment-edfi [assessmentName]
  (let [tmp (element :Assessment {:id (assessment-id assessmentName)}
      (element :AssessmentTitle {} assessmentName)
      (element :AssessmentIdentificationCode {:IdentificationSystem "Test Contractor"}
        (element :ID {} assessmentName)
      )
      (element :AssessmentCategory {} "State summative assessment 3-8 general")
      (element :AcademicSubject {} "Mathematics")
      (element :GradeLevelAssessed {} "Seventh grade")
      (element :AssessmentPerformanceLevel {}
        (element :PerformanceLevel {}
          (element :CodeValue {} "W")
        )
        (element :AssessmentReportingMethod {} "Scale score")
        (element :MinimumScore {} "6")
        (element :MaximumScore {} "14")
      )
      (element :AssessmentPerformanceLevel {}
        (element :PerformanceLevel {}
          (element :CodeValue {} "B")
        )
        (element :AssessmentReportingMethod {} "Scale score")
        (element :MinimumScore {} "15")
        (element :MaximumScore {} "20")
      )
      (element :AssessmentPerformanceLevel {}
        (element :PerformanceLevel {}
          (element :CodeValue {} "S")
        )
        (element :AssessmentReportingMethod {} "Scale score")
        (element :MinimumScore {} "21")
        (element :MaximumScore {} "27")
      )
      (element :AssessmentPerformanceLevel {}
        (element :PerformanceLevel {}
          (element :CodeValue {} "E")
        )
        (element :AssessmentReportingMethod {} "Scale score")
        (element :MinimumScore {} "28")
        (element :MaximumScore {} "33")
      )
      (element :ContentStandard {} "State Standard")
      (element :Version {} "1")
      (element :RevisionDate {} "2011-03-12")
      (element :MaxRawScore {} "33")
    )]
    tmp
  )
)

(defn create-session-edfi [schoolName]
  (into () [
    (element :CalendarDate {:id (calendar-date-id schoolName) }
      (element :Date {} "2011-09-22")
      (element :CalendarEvent {} "Instructional day")
    )

    (element :GradingPeriod {:id (grading-period-id schoolName)}
      (element :GradingPeriodIdentity {}
        (element :GradingPeriod {} "End of Year")
        (element :SchoolYear {} "2011-2012")
        (element :StateOrganizationId {} schoolName)
      )

      (element :BeginDate {} "2011-09-01")
      (element :EndDate {} "2011-12-01")
      (element :TotalInstructionalDays {} "90")
      (element :CalendarDateReference {:ref (calendar-date-id schoolName)})
    )

    (element :Session {}
      (element :SessionName {} (session-id schoolName))
      (element :SchoolYear {} "2011-2012")
      (element :Term {} "Fall Semester")
      (element :BeginDate {} "2011-09-06")
      (element :EndDate {} "2011-12-16")
      (element :TotalInstructionalDays {} "75")
      (ed-org-ref schoolName)
      (element :GradingPeriodReference {:ref (grading-period-id schoolName)})
    ) ]
  )
)

(defn create-course-offering-edfi [districtName schoolName]
  (element :CourseOffering { :id (local-course-id districtName schoolName) }
    (element :LocalCourseCode {} (local-course-id districtName schoolName))
    (element :LocalCourseTitle {} "7th Grade Math")
    (school-ref schoolName)
    (session-ref schoolName)
    (course-ref districtName)
  )
)

(defn create-section-edfi [districtName schoolName]
  (element :Section {}
    (element :UniqueSectionCode {} (section-id schoolName))
    (element :SequenceOfCourse {} "1")
    (element :CourseOfferingReference { :ref (local-course-id districtName schoolName) })
    (school-ref schoolName)
    (session-ref schoolName)
  ) 
)

(defn create-student-enrollment-edfi [schoolName studentId]
  (into () [
    (create-student-school-association-edfi schoolName studentId)
    (create-student-section-association-edfi schoolName studentId) ]
  )
)

(defn gen-sessions-edfi [districtName schools]
  (gen-edfi-xml :InterchangeEducationOrgCalendar (format "/tmp/test/E-%s-calendar.xml" districtName)
    (reverse
      (into ()
        [
          (for [schoolName schools
            :let [tmp (create-session-edfi schoolName)]]
            tmp
          )
        ]
      )
    )
  )
)

(defn gen-students-edfi [districtName schools rng]
  (gen-edfi-xml :InterchangeStudentParent (format "/tmp/test/C-%s-students.xml" districtName)
    (into ()
      [
        (for [schoolName schools]
          (for [id rng
            :let [tmp (create-student-edfi schoolName id)]
            ]
            tmp
          )
        )
      ]
    )
  )
)


(defn gen-state-edfi []
  (gen-edfi-xml :InterchangeEducationOrganization "/tmp/test/A-state.xml" (create-state-edfi))
)
  

(defn gen-schools-edfi [districtName schools]
  (gen-edfi-xml :InterchangeEducationOrganization (format "/tmp/test/D-%s-schools.xml" districtName)
    (reverse
      (into ()
        [
          (create-district-edfi districtName)
          (create-course-edfi districtName)
          (for [schoolName schools
            :let [tmp (create-school-edfi districtName schoolName)]]
            tmp
          )
        ]
      )
    )
  )
)

(defn gen-assessment-edfi [assessmentName]
  (gen-edfi-xml :InterchangeAssessmentMetadata "/tmp/test/B-assessment.xml" (create-assessment-edfi assessmentName))
)

(defn gen-student-assessment-associations-edfi [districtName schools students assessment n]
  (gen-edfi-xml :InterchangeStudentAssessment (format "/tmp/test/H-%s-assessment-results.xml" districtName)
    (for [schoolName schools]
      (for [studentId students, i (range 1 (inc n))]
        (create-student-assessment-association-edfi schoolName studentId assessment (str "2011-10-" (format "%02d" i)))
      )
    )
  )
)

(defn gen-student-enrollments-edfi [districtName schools rng]
  (gen-edfi-xml :InterchangeStudentEnrollment (format "/tmp/test/G-%s-enrollment.xml" districtName)
    (for [schoolName schools]
      (for [id rng]
        (create-student-enrollment-edfi schoolName id)
      )
    )
  )
)

(defn gen-sections-edfi [districtName schools]
  (gen-edfi-xml :InterchangeMasterSchedule (format "/tmp/test/F-%s-master-schedule.xml" districtName)
    (reverse 
      (into () 
        [
          (for [schoolName schools]
            (create-course-offering-edfi districtName schoolName)
          )
          (for [schoolName schools]
            (create-section-edfi districtName schoolName)
          )
        ]
      )
    )
  )
)

(defn md5 [file]
  (let [input (java.io.FileInputStream. file)
    digest (java.security.MessageDigest/getInstance "MD5")
    stream (java.security.DigestInputStream. input digest) bufsize (* 1024 1024) buf (byte-array bufsize)]
    (while (not= -1 (.read stream buf 0 bufsize)))
    (apply str (map (partial format "%02x") (.digest digest)))
  )
)

(defn get-md5-for-file [file]
  (def md5string (md5 file))
  (def filename (.getName file))
  (def formatString (str "edfi-xml,%s,%s,%s"))
  (let [rval (str
      (if (substring? "-assessment.xml" filename)
        (format formatString "AssessmentMetadata" filename md5string)
      )
      (if (substring? "-state.xml" filename)
        (format formatString "EducationOrganization" filename md5string)
      )
      (if (substring? "-schools.xml" filename)
        (format formatString "EducationOrganization" filename md5string)
      )
      (if (substring? "-calendar.xml" filename)
        (format formatString "EducationOrgCalendar" filename md5string)
      )
      (if (substring? "-master-schedule.xml" filename)
        (format formatString "MasterSchedule" filename md5string)
      )
      (if (substring? "-students.xml" filename)
        (format formatString "StudentParent" filename, md5string)
      )
      (if (substring? "-enrollment.xml" filename)
        (format formatString "StudentEnrollment" filename md5string)
      )
      (if (substring? "-assessment-results.xml" filename)
        (format formatString "StudentAssessment" filename md5string)
      )
      ; need
      ; edfi-xml,StaffAssociation,InterchangeStaffAssociation.xml,c5efbe159ac926629a3b494460243aba
    )]
    (str rval)
  )
)

(defn create-control-file-edfi []
  (def directory (clojure.java.io/file "/tmp/test"))
  (def files (rest (file-seq directory)))
  (with-open [out (java.io.PrintWriter. (java.io.FileOutputStream. "/tmp/test/MainControlFile.ctl"))]
    (doseq [file files]
      (def tmp (get-md5-for-file file))
      (.println out tmp)
    )
  )
)

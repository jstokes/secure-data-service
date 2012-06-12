
Feature: Student Access Security Mega Test
I want to test all combinations and permutations of accessing student data

Scenario Outline: Teacher attempt to access students through sections
  Given I am user <User> in IDP "SEC"
  When I make an API call to get the student <Student>
    Then I should receive a return code of <Read Code>
    And I see the response <Restricted Data> restricted data and <General Data> general data
    When I make an API call to update the student <Student>
    Then I should receive a return code of <Write Code>
  Examples:
| User     | Student        | Read Code | Restricted Data | General Data | Write Code |Comment |
| "teach1" | "student01"     | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through future end-date section and future end-date enrollment |
| "teach2" | "student01"     | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through future end-date section and future end-date enrollment |
| "teach3" | "student01"     | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through future end-date section and future end-date enrollment |
| "teach4" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date section and future end-date enrollment |
| "teach1" | "student02"     | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through future end-date section and no end-date enrollment |
| "teach2" | "student02"     | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through future end-date section and no end-date enrollment |
| "teach3" | "student02"     | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through future end-date section and no end-date enrollment |
| "teach4" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date section and no end-date enrollment |
| "teach1" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date section and past end-date enrollment |
| "teach2" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date section and past end-date enrollment |
| "teach3" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date section and past end-date enrollment |
| "teach4" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date section and past end-date enrollment |
| "teach1" | "student04"     | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through no end-date section and future end-date enrollment |
| "teach2" | "student04"     | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through no end-date section and future end-date enrollment |
| "teach3" | "student04"     | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through no end-date section and future end-date enrollment |
| "teach4" | "student04"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date section and future end-date enrollment |
| "teach1" | "student05"     | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through no end-date section and no end-date enrollment |
| "teach2" | "student05"     | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through no end-date section and no end-date enrollment |
| "teach3" | "student05"     | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through no end-date section and no end-date enrollment |
| "teach4" | "student05"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date section and no end-date enrollment |
| "teach1" | "student06"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date section and past end-date enrollment |
| "teach2" | "student06"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date section and past end-date enrollment |
| "teach3" | "student06"     | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date section and past end-date enrollment |
| "teach4" | "student06"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date section and past end-date enrollment |
| "teach1" | "student07"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date section and future end-date enrollment |
| "teach2" | "student07"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date section and future end-date enrollment |
| "teach3" | "student07"     | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date section and future end-date enrollment |
| "teach4" | "student07"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date section and future end-date enrollment |
| "teach1" | "student08"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date section and no end-date enrollment |
| "teach2" | "student08"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date section and no end-date enrollment |
| "teach3" | "student08"     | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date section and no end-date enrollment |
| "teach4" | "student08"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date section and no end-date enrollment |
| "teach1" | "student09"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date section and past end-date enrollment |
| "teach2" | "student09"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date section and past end-date enrollment |
| "teach3" | "student09"     | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date section and past end-date enrollment |
| "teach4" | "student09"     | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date section and past end-date enrollment |
| "teach1" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no section assoc and future end-date enrollment |
| "teach2" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no section assoc and future end-date enrollment |
| "teach3" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no section assoc and future end-date enrollment |
| "teach4" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no section assoc and future end-date enrollment |

Scenario Outline: Teacher views students through Cohort

  Given I am user <User> in IDP "SEC"
  When I make an API call to get the student <Student>
    Then I should receive a return code of <Read Code>
    And I see the response <Restricted Data> restricted data and <General Data> general data
    When I make an API call to update the student <Student>
    Then I should receive a return code of <Write Code>
  Examples:
| User     | Student        | Read Code | Restricted Data | General Data | Write Code |Comment |
| "teach1" | "student11"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through future end-date, record-access true cohort and future end-date enrollment |
| "teach2" | "student11"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through future end-date, record-access true cohort and future end-date enrollment |
| "teach3" | "student11"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through future end-date, record-access true cohort and future end-date enrollment |
| "teach4" | "student11"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access true cohort and future end-date enrollment |
| "teach1" | "student12"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through future end-date, record-access true cohort and no end-date enrollment |
| "teach2" | "student12"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through future end-date, record-access true cohort and no end-date enrollment |
| "teach3" | "student12"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through future end-date, record-access true cohort and no end-date enrollment |
| "teach4" | "student12"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access true cohort and no end-date enrollment |
| "teach1" | "student13"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access true cohort and past end-date enrollment |
| "teach2" | "student13"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access true cohort and past end-date enrollment |
| "teach3" | "student13"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access true cohort and past end-date enrollment |
| "teach4" | "student13"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access true cohort and past end-date enrollment |
| "teach1" | "student14"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through no end-date, record-access true cohort and future end-date enrollment |
| "teach2" | "student14"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through no end-date, record-access true cohort and future end-date enrollment |
| "teach3" | "student14"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through no end-date, record-access true cohort and future end-date enrollment |
| "teach4" | "student14"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access true cohort and future end-date enrollment |
| "teach1" | "student15"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through no end-date, record-access true cohort and no end-date enrollment |
| "teach2" | "student15"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through no end-date, record-access true cohort and no end-date enrollment |
| "teach3" | "student15"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through no end-date, record-access true cohort and no end-date enrollment |
| "teach4" | "student15"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access true cohort and no end-date enrollment |
| "teach1" | "student16"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access true cohort and past end-date enrollment |
| "teach2" | "student16"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access true cohort and past end-date enrollment |
| "teach3" | "student16"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access true cohort and past end-date enrollment |
| "teach4" | "student16"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access true cohort and past end-date enrollment |
| "teach1" | "student17"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access true cohort and future end-date enrollment |
| "teach2" | "student17"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access true cohort and future end-date enrollment |
| "teach3" | "student17"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access true cohort and future end-date enrollment |
| "teach4" | "student17"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access true cohort and future end-date enrollment |
| "teach1" | "student18"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access true cohort and no end-date enrollment |
| "teach2" | "student18"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access true cohort and no end-date enrollment |
| "teach3" | "student18"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access true cohort and no end-date enrollment |
| "teach4" | "student18"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access true cohort and no end-date enrollment |
| "teach1" | "student19"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access true cohort and past end-date enrollment |
| "teach2" | "student19"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access true cohort and past end-date enrollment |
| "teach3" | "student19"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access true cohort and past end-date enrollment |
| "teach4" | "student19"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access true cohort and past end-date enrollment |
| "teach1" | "student20"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access false cohort and future end-date enrollment |
| "teach2" | "student20"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access false cohort and future end-date enrollment |
| "teach3" | "student20"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access false cohort and future end-date enrollment |
| "teach4" | "student20"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access false cohort and future end-date enrollment |
| "teach1" | "student21"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access false cohort and no end-date enrollment |
| "teach2" | "student21"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access false cohort and no end-date enrollment |
| "teach3" | "student21"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access false cohort and no end-date enrollment |
| "teach4" | "student21"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access false cohort and no end-date enrollment |
| "teach1" | "student22"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access false cohort and past end-date enrollment |
| "teach2" | "student22"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access false cohort and past end-date enrollment |
| "teach3" | "student22"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access false cohort and past end-date enrollment |
| "teach4" | "student22"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access false cohort and past end-date enrollment |
| "teach1" | "student23"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access false cohort and future end-date enrollment |
| "teach2" | "student23"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access false cohort and future end-date enrollment |
| "teach3" | "student23"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access false cohort and future end-date enrollment |
| "teach4" | "student23"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access false cohort and future end-date enrollment |
| "teach1" | "student24"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access false cohort and no end-date enrollment |
| "teach2" | "student24"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access false cohort and no end-date enrollment |
| "teach3" | "student24"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access false cohort and no end-date enrollment |
| "teach4" | "student24"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access false cohort and no end-date enrollment |
| "teach1" | "student25"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access false cohort and past end-date enrollment |
| "teach2" | "student25"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access false cohort and past end-date enrollment |
| "teach3" | "student25"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access false cohort and past end-date enrollment |
| "teach4" | "student25"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access false cohort and past end-date enrollment |
| "teach1" | "student26"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access false cohort and future end-date enrollment |
| "teach2" | "student26"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access false cohort and future end-date enrollment |
| "teach3" | "student26"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access false cohort and future end-date enrollment |
| "teach4" | "student26"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access false cohort and future end-date enrollment |
| "teach1" | "student27"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access false cohort and no end-date enrollment |
| "teach2" | "student27"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access false cohort and no end-date enrollment |
| "teach3" | "student27"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access false cohort and no end-date enrollment |
| "teach4" | "student27"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access false cohort and no end-date enrollment |
| "teach1" | "student28"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access false cohort and past end-date enrollment |
| "teach2" | "student28"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access false cohort and past end-date enrollment |
| "teach3" | "student28"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access false cohort and past end-date enrollment |
| "teach4" | "student28"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access false cohort and past end-date enrollment |
| "teach1" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no cohort assoc and future end-date enrollment |
| "teach2" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no cohort assoc and future end-date enrollment |
| "teach3" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no cohort assoc and future end-date enrollment |
| "teach4" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no cohort assoc and future end-date enrollment |

Scenario Outline: Teacher views students through program

  Given I am user <User> in IDP "SEC"
  When I make an API call to get the student <Student>
    Then I should receive a return code of <Read Code>
    And I see the response <Restricted Data> restricted data and <General Data> general data
    When I make an API call to update the student <Student>
    Then I should receive a return code of <Write Code>
  Examples:
| User     | Student        | Read Code | Restricted Data | General Data | Write Code |Comment |
| "teach1" | "student29"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through future end-date, record-access true program and future end-date enrollment |
| "teach2" | "student29"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through future end-date, record-access true program and future end-date enrollment |
| "teach3" | "student29"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through future end-date, record-access true program and future end-date enrollment |
| "teach4" | "student29"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access true program and future end-date enrollment |
| "teach1" | "student30"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through future end-date, record-access true program and no end-date enrollment |
| "teach2" | "student30"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through future end-date, record-access true program and no end-date enrollment |
| "teach3" | "student30"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through future end-date, record-access true program and no end-date enrollment |
| "teach4" | "student30"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access true program and no end-date enrollment |
| "teach1" | "student31"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access true program and past end-date enrollment |
| "teach2" | "student31"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access true program and past end-date enrollment |
| "teach3" | "student31"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access true program and past end-date enrollment |
| "teach4" | "student31"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access true program and past end-date enrollment |
| "teach1" | "student32"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through no end-date, record-access true program and future end-date enrollment |
| "teach2" | "student32"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through no end-date, record-access true program and future end-date enrollment |
| "teach3" | "student32"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through no end-date, record-access true program and future end-date enrollment |
| "teach4" | "student32"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access true program and future end-date enrollment |
| "teach1" | "student33"    | 200       | "excludes"      | "includes"   | 403        | teacher as Educator access student through no end-date, record-access true program and no end-date enrollment |
| "teach2" | "student33"    | 200       | "includes"      | "includes"   | 403        | teacher as Leader   access student through no end-date, record-access true program and no end-date enrollment |
| "teach3" | "student33"    | 200       | "includes"      | "includes"   | 204        | teacher as IT Admin access student through no end-date, record-access true program and no end-date enrollment |
| "teach4" | "student33"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access true program and no end-date enrollment |
| "teach1" | "student34"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access true program and past end-date enrollment |
| "teach2" | "student34"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access true program and past end-date enrollment |
| "teach3" | "student34"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access true program and past end-date enrollment |
| "teach4" | "student34"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access true program and past end-date enrollment |
| "teach1" | "student35"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access true program and future end-date enrollment |
| "teach2" | "student35"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access true program and future end-date enrollment |
| "teach3" | "student35"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access true program and future end-date enrollment |
| "teach4" | "student35"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access true program and future end-date enrollment |
| "teach1" | "student36"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access true program and no end-date enrollment |
| "teach2" | "student36"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access true program and no end-date enrollment |
| "teach3" | "student36"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access true program and no end-date enrollment |
| "teach4" | "student36"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access true program and no end-date enrollment |
| "teach1" | "student37"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access true program and past end-date enrollment |
| "teach2" | "student37"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access true program and past end-date enrollment |
| "teach3" | "student37"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access true program and past end-date enrollment |
| "teach4" | "student37"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access true program and past end-date enrollment |
| "teach1" | "student38"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access false program and future end-date enrollment |
| "teach2" | "student38"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access false program and future end-date enrollment |
| "teach3" | "student38"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access false program and future end-date enrollment |
| "teach4" | "student38"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access false program and future end-date enrollment |
| "teach1" | "student39"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access false program and no end-date enrollment |
| "teach2" | "student39"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access false program and no end-date enrollment |
| "teach3" | "student39"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access false program and no end-date enrollment |
| "teach4" | "student39"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access false program and no end-date enrollment |
| "teach1" | "student40"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through future end-date, record-access false program and past end-date enrollment |
| "teach2" | "student40"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through future end-date, record-access false program and past end-date enrollment |
| "teach3" | "student40"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through future end-date, record-access false program and past end-date enrollment |
| "teach4" | "student40"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through future end-date, record-access false program and past end-date enrollment |
| "teach1" | "student41"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access false program and future end-date enrollment |
| "teach2" | "student41"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access false program and future end-date enrollment |
| "teach3" | "student41"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access false program and future end-date enrollment |
| "teach4" | "student41"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access false program and future end-date enrollment |
| "teach1" | "student42"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access false program and no end-date enrollment |
| "teach2" | "student42"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access false program and no end-date enrollment |
| "teach3" | "student42"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access false program and no end-date enrollment |
| "teach4" | "student42"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access false program and no end-date enrollment |
| "teach1" | "student43"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no end-date, record-access false program and past end-date enrollment |
| "teach2" | "student43"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no end-date, record-access false program and past end-date enrollment |
| "teach3" | "student43"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no end-date, record-access false program and past end-date enrollment |
| "teach4" | "student43"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no end-date, record-access false program and past end-date enrollment |
| "teach1" | "student44"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access false program and future end-date enrollment |
| "teach2" | "student44"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access false program and future end-date enrollment |
| "teach3" | "student44"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access false program and future end-date enrollment |
| "teach4" | "student44"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access false program and future end-date enrollment |
| "teach1" | "student45"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access false program and no end-date enrollment |
| "teach2" | "student45"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access false program and no end-date enrollment |
| "teach3" | "student45"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access false program and no end-date enrollment |
| "teach4" | "student45"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access false program and no end-date enrollment |
| "teach1" | "student46"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through past end-date, record-access false program and past end-date enrollment |
| "teach2" | "student46"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through past end-date, record-access false program and past end-date enrollment |
| "teach3" | "student46"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through past end-date, record-access false program and past end-date enrollment |
| "teach4" | "student46"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through past end-date, record-access false program and past end-date enrollment |
| "teach1" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Educator access student through no program assoc and future end-date enrollment |
| "teach2" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Leader   access student through no program assoc and future end-date enrollment |
| "teach3" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as IT Admin access student through no program assoc and future end-date enrollment |
| "teach4" | "student10"    | 403       | "excludes"      | "excludes"   | 403        | teacher as Agg View access student through no program assoc and future end-date enrollment |


Scenario Outline: Staff attempts to access specific students as various roles

Given I am user <User> in IDP "SEC"
When I make an API call to get the student <Student>
  Then I should receive a return code of <Read Code>
    And I see the response <Restricted Data> restricted data and <General Data> general data
  When I make an API call to update the student <Student>
  Then I should receive a return code of <Write Code>
Examples:
| User      | Student        | Read Code | Restricted Data | General Data | Write Code |Comment |
| "sstaff1" | "student01"     | 200       | "excludes"      | "includes"   | 403        | school-staff as Educator access student because the student is currently enrolled |
| "sstaff2" | "student01"     | 200       | "includes"      | "includes"   | 403        | school-staff as Leader   access student because the student is currently enrolled |
| "sstaff3" | "student01"     | 200       | "includes"      | "includes"   | 204        | school-staff as IT Admin access student because the student is currently enrolled |
| "sstaff4" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | school-staff as Agg View access student because the student is currently enrolled |
| "sstaff5" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't access student because the staff was terminated |
| "sstaff6" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't access student because the staff was not associated |


| "sstaff1" | "student02"     | 200       | "excludes"      | "includes"   | 403        | school-staff as Educator access student because the student is currently enrolled with no end-date |
| "sstaff2" | "student02"     | 200       | "includes"      | "includes"   | 403        | school-staff as Leader   access student because the student is currently enrolled with no end-date |
| "sstaff3" | "student02"     | 200       | "includes"      | "includes"   | 204        | school-staff as IT Admin access student because the student is currently enrolled with no end-date |
| "sstaff4" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | school-staff as Agg View access student because the student is currently enrolled with no end-date |
| "sstaff5" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't access student because the staff was terminated |
| "sstaff6" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't access student because the staff was not associated |


| "sstaff1" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | school-staff as Educator can't access student because the student is not currently enrolled |
| "sstaff2" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | school-staff as Leader   can't access student because the student is not currently enrolled |
| "sstaff3" | "student03"     | 403       | "excludes"      | "excludes"   | 204        | school-staff as IT Admin can't access student because the student is not currently enrolled |
| "sstaff4" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | school-staff as Agg View can't access student because the student is not currently enrolled |
| "sstaff5" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't access student because the staff was terminated |
| "sstaff5" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't access student because the staff was terminated |
| "sstaff6" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't access student because the staff was not associated |

| "dstaff1" | "student01"     | 200       | "excludes"      | "includes"   | 403        | district-staff as Educator access student because the student is currently enrolled |
| "dstaff2" | "student01"     | 200       | "includes"      | "includes"   | 403        | district-staff as Leader   access student because the student is currently enrolled |
| "dstaff3" | "student01"     | 200       | "includes"      | "includes"   | 204        | district-staff as IT Admin access student because the student is currently enrolled |
| "dstaff4" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | district-staff as Agg View access student because the student is currently enrolled |
| "dstaff5" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can't access student because the staff was terminated |
| "dstaff6" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can't access student because the staff was not associated |

| "dstaff1" | "student02"     | 200       | "excludes"      | "includes"   | 403        | district-staff as Educator access student because the student is currently enrolled with no end-date |
| "dstaff2" | "student02"     | 200       | "includes"      | "includes"   | 403        | district-staff as Leader   access student because the student is currently enrolled with no end-date |
| "dstaff3" | "student02"     | 200       | "includes"      | "includes"   | 204        | district-staff as IT Admin access student because the student is currently enrolled with no end-date |
| "dstaff4" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | district-staff as Agg View access student because the student is currently enrolled with no end-date |
| "dstaff5" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can't access student because the staff was terminated |
| "dstaff6" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can't access student because the staff was not associated |

| "dstaff1" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | district-staff as Educator can't access student because the student is not currently enrolled |
| "dstaff2" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | district-staff as Leader   can't access student because the student is not currently enrolled |
| "dstaff3" | "student03"     | 403       | "excludes"      | "excludes"   | 204        | district-staff as IT Admin can't access student because the student is not currently enrolled |
| "dstaff4" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | district-staff as Agg View can't access student because the student is not currently enrolled |
| "dstaff5" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can't access student because the staff was terminated |
| "dstaff6" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can't access student because the staff was not associated |



| "Sstaff1" | "student01"     | 200       | "excludes"      | "includes"   | 403        | state-staff as Educator access student because the student is currently enrolled |
| "Sstaff2" | "student01"     | 200       | "includes"      | "includes"   | 403        | state-staff as Leader   access student because the student is currently enrolled |
| "Sstaff3" | "student01"     | 200       | "includes"      | "includes"   | 204        | state-staff as IT Admin access student because the student is currently enrolled |
| "Sstaff4" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | state-staff as Agg View access student because the student is currently enrolled |
| "Sstaff5" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can't access student because the staff was terminated |
| "Sstaff6" | "student01"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can't access student because the staff was not associated |

| "Sstaff1" | "student02"     | 200       | "excludes"      | "includes"   | 403        | state-staff as Educator access student because the student is currently enrolled with no end-date |
| "Sstaff2" | "student02"     | 200       | "includes"      | "includes"   | 403        | state-staff as Leader   access student because the student is currently enrolled with no end-date |
| "Sstaff3" | "student02"     | 200       | "includes"      | "includes"   | 204        | state-staff as IT Admin access student because the student is currently enrolled with no end-date |
| "Sstaff4" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | state-staff as Agg View access student because the student is currently enrolled with no end-date |
| "Sstaff5" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can't access student because the staff was terminated |
| "Sstaff6" | "student02"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can't access student because the staff was not associated |


| "Sstaff1" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | state-staff as Educator can't access student because the student is not currently enrolled |
| "Sstaff2" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | state-staff as Leader   can't access student because the student is not currently enrolled |
| "Sstaff3" | "student03"     | 403       | "excludes"      | "excludes"   | 204        | state-staff as IT Admin can't access student because the student is not currently enrolled |
| "Sstaff4" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | state-staff as Agg View can't access student because the student is not currently enrolled |
| "Sstaff5" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can't access student because the staff was terminated |
| "Sstaff6" | "student03"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can't access student because the staff was not associated |


| "sstaff1" | "studentX"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't see student in another school within tenant |
| "sstaff1" | "studentX"     | 403       | "excludes"      | "excludes"   | 403        | school-staff can't see student in another school outside tenant |
| "dstaff1" | "studentX"     | 200       | "includes"      | "excludes"   | 403        | district-staff can see student in another school within district |
| "dstaff1" | "studentX"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can' see student in another school outside district |
| "dstaff1" | "studentX"     | 403       | "excludes"      | "excludes"   | 403        | district-staff can't see student in another school outside tenant |
| "Sstaff1" | "studentX"     | 200       | "includes"      | "excludes"   | 403        | state-staff can see student in school within state |
| "Sstaff1" | "studentX"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can' see student in another school outside state |
| "Sstaff1" | "studentX"     | 403       | "excludes"      | "excludes"   | 403        | state-staff can't see student in another school outside tenant |



Scenario Outline: Staff accessing lists of students at differing levels
  Given I am user <User> in IDP "SEC"
  When I make an API call to get my student list
  Then I should see a count of <Count>
Examples:
| User      | Count | Comment |
| "sstaff1" | 0     | School-staff should see all students currently enrolled at the school. |
| "dstaff1" | 0     | District-staff should see all students currently enrolled at the schools in their district. |
| "Sstaff1" | 0     | State-staff should see all students currently enrolled at the schools in their state. |

package org.slc.sli.entity;

import org.slc.sli.util.Constants;

/**
 * 
 * TODO: Write Javadoc
 *
 */
public class Student {

    private String id, studentUniqueStateId, sex, economicDisadvantaged, limitedEnglishProficiency, schoolFoodServicesEligibility;
    
    public void setLimitedEnglishProficiency(String limitedEnglishProficiency) {
        this.limitedEnglishProficiency = limitedEnglishProficiency;
    }

    public void setSchoolFoodServicesEligibility(String schoolFoodServicesEligibility) {
        this.schoolFoodServicesEligibility = schoolFoodServicesEligibility;
    }

    private static String[] studentEntityProgramCodes = {Constants.PROGRAM_ELL, Constants.PROGRAM_FRE};
    
    private NameData name;
    public NameData getName() {
        return name;
    }

    public void setName(NameData name) {
        this.name = name;
        
    }

    public BirthData getBirthData() {
        return birthData;
    }

    public void setBirthData(BirthData birthData) {
        this.birthData = birthData;
    }

    private BirthData birthData;
    
    private class BirthData {
        String birthDate;
        
        @Override
        public String toString() {
            return birthDate;
        }
    }
    
    
    public String getFirstName() {
        return name.getFirstName();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return name.getLastSurname();
    }
    
    private class NameData {
        private String firstName, middleName, lastSurname;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastSurname() {
            return lastSurname;
        }

        public void setLastSurname(String lastSurname) {
            this.lastSurname = lastSurname;
        }
    
    }
    
    public static String[] getProgramCodesForStudent() {
        return studentEntityProgramCodes;
    }

    
    public boolean getProgramParticipation(String programCode) {
        if (programCode.equals(Constants.PROGRAM_ELL))
            return isLimitedEnglishProficient();
        
        if (programCode.equals(Constants.PROGRAM_FRE))
            return isSchoolFoodServiceEligile();
        
        return false;
    }
    
    private boolean isLimitedEnglishProficient() {
        return (limitedEnglishProficiency != null) && (limitedEnglishProficiency.equals("Yes"));
    }
    
    
    private boolean isSchoolFoodServiceEligile() {
        return (schoolFoodServicesEligibility != null) && (schoolFoodServicesEligibility.equals("Free"));
    }
    
    
    
}

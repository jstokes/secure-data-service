package org.slc.sli.view;

import org.slc.sli.entity.Assessment;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Calendar;

/**
 * A static class for views in SLI dashboard to perform "timed" business logics
 * 
 * @author syau
 *
 */
public class TimedLogic {

    // These implementations are *all* temporary. 
    // We can implement this only after we have a final spec from the API team on what the 
    // Assessment entity really looks like. For now we're just going by the mock assessment entity. 

    /**
     * Returns the assessment with the most recent timestamp
     */
    public static Assessment getMostRecentAssessment(List<Assessment> a) {
        Collections.sort(a, new Comparator<Assessment>() {
            // this should probably get more precise if we actually have an actual timestamp!
            public int compare(Assessment o1, Assessment o2) {
                return o1.getYear() - o2.getYear();  
            }
        });
        return a.get(0);
    }

    /**
     * Returns the assessment with the highest score
     */
    public static Assessment getHighestEverAssessment(List<Assessment> a) {
        Collections.sort(a, new Comparator<Assessment>() {
            public int compare(Assessment o1, Assessment o2) {
                return o1.getScaleScore() - o2.getScaleScore();
            }
        });
        return a.get(0);
    }

    /**
     * Returns the assessment from the latest window
     */
    // pretty sure you'll need the assessment metat data for this... but whateves.
    // For now, just pretend all assessments are administered once a year in January 01.
    public static Assessment getMostRecentAssessmentWindow(List<Assessment> a) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (Assessment ass : a) {
            if (ass.getYear() == year) {
                return ass;
            }
        }
        return null;
    }

}

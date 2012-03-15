package org.slc.sli.view;

import org.slc.sli.config.DisplaySet;
import org.slc.sli.config.Field;
import org.slc.sli.config.ViewConfig;
import org.slc.sli.entity.GenericEntity;

import java.util.SortedSet;

/**
 * Class to manage gradebook entry data for view config that needs data added dynamically
 * @author jstokes
 */
public class GradebookEntryViewManager {

    private SortedSet<GenericEntity> gradebookIds;

    private static final String CURRENT = "Current";
    private static final String GRADES = "<center>Unit Tests</center>";
    private static final String AVERAGE = "Average";
    private static final String DATE_FULFILLED = "dateFulfilled";
    private static final String CURRENT_TERM = "currentTermGrade";
    private static final String UNIT_TEST_GRADE = "unitTestGrade";

    /**
     * Constructor
     */
    public GradebookEntryViewManager(SortedSet<GenericEntity> gradebookIds) {
        this.gradebookIds = gradebookIds;
    }

    /**
     * Adds columns for gradebook entries, including current average
     * and previous unit test grades
     * @param view The view to manipulate
     * @return view with added columns
     */
    public ViewConfig addGradebookEntries(ViewConfig view) {
        view.getDisplaySet().add(createCurrentGrade());
        view.getDisplaySet().add(createUnitTests());

        return view;
    }
    
    private DisplaySet createCurrentGrade() {
        DisplaySet currentGrade = new DisplaySet();
        currentGrade.setDisplayName(CURRENT);
        currentGrade.getField().add(createCurrentTermGrade());

        return currentGrade;
    }

    private DisplaySet createUnitTests() {
        DisplaySet unitTests = new DisplaySet();
        unitTests.setDisplayName(GRADES);

        for (GenericEntity entry : gradebookIds) {
            Field unitField = createUnitTest(entry.getString(DATE_FULFILLED), entry.getString(DATE_FULFILLED));
            unitTests.getField().add(unitField);
        }

        return unitTests;
    }
    
    private Field createCurrentTermGrade() {
        Field currentTermGrade = new Field();

        currentTermGrade.setDisplayName(AVERAGE);
        currentTermGrade.setType(CURRENT_TERM);
        currentTermGrade.setTimeSlot(CURRENT);
        currentTermGrade.setValue("CurrentTermGrade");
        
        return currentTermGrade;
    }

    private Field createUnitTest(String testName, String testId) {
        Field unitTest = new Field();

        unitTest.setDisplayName(testName);
        unitTest.setType(UNIT_TEST_GRADE);
        unitTest.setTimeSlot(testId);
        unitTest.setValue("UnitTest");

        return unitTest;
    }
}

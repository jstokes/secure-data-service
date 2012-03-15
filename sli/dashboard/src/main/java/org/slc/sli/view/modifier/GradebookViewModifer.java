package org.slc.sli.view.modifier;

import org.slc.sli.config.DisplaySet;
import org.slc.sli.config.Field;
import org.slc.sli.config.ViewConfig;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.view.GradebookEntryResolver;

import java.util.SortedSet;

/**
 * Class to manage gradebook entry data for view config that needs data added dynamically
 * @author jstokes
 */
public class GradebookViewModifer implements ViewModifier {

    private SortedSet<GenericEntity> gradebookIds;

    protected static final String CURRENT = "Current";
    protected static final String GRADES = "<center>Unit Tests</center>";
    protected static final String AVERAGE = "Average";
    protected static final String DATE_FULFILLED = "dateFulfilled";
    protected static final String CURRENT_TERM = "currentTermGrade";
    protected static final String UNIT_TEST_GRADE = "unitTestGrade";

    /**
     * Constructor
     */
    public GradebookViewModifer(GradebookEntryResolver gradebookEntryResolver) {
        this.gradebookIds = gradebookEntryResolver.getGradebookIds();
    }

    /**
     * Delegates to the addGradebookEntries method to add the necessary columns to the specified view
     * @param view The view to modify
     * @return A modified view
     */
    @Override
    public ViewConfig modify(ViewConfig view) {
        return addGradebookEntries(view);
    }

    /**
     * Adds columns for gradebook entries, including current average
     * and previous unit test grades
     * @param view The view to manipulate
     * @return view with added columns
     */
    private ViewConfig addGradebookEntries(ViewConfig view) {
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
            Field unitField = createUnitTest(entry.getString(DATE_FULFILLED));
            unitTests.getField().add(unitField);
        }

        return unitTests;
    }
    
    private Field createCurrentTermGrade() {
        Field currentTermGrade = new Field();

        currentTermGrade.setDisplayName(AVERAGE);
        currentTermGrade.setType(CURRENT_TERM);
        currentTermGrade.setTimeSlot(CURRENT);
        currentTermGrade.setValue(CURRENT_TERM);
        
        return currentTermGrade;
    }

    private Field createUnitTest(String testName) {
        Field unitTest = new Field();

        unitTest.setDisplayName(testName);
        unitTest.setType(UNIT_TEST_GRADE);
        unitTest.setTimeSlot(testName);
        unitTest.setValue(testName);

        return unitTest;
    }
}

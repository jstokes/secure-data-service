package org.slc.sli.view.widget;

import java.util.List;
import java.util.Map;

import org.slc.sli.config.Field;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.util.Constants;
import org.slc.sli.view.AssessmentResolver;

/**
 * Logic used by a widget that displays assessment results color-coded by performance level and
 * shows rectangles sized corresponding to scale score.
 *
 * @author syau
 */
public class FuelGauge {

    private Field field;
    private Map student;
    private AssessmentResolver assmts;
    private ColorByPerf colorByPerf;

    public FuelGauge(Field field, Map student, AssessmentResolver assmts) {
        this.field = field;
        this.student = student;
        this.assmts = assmts;
        this.colorByPerf = WidgetFactory.createColorByPerf(field, student, assmts);
    }

    /**
     * Get the color index for display
     */
    public int getColorIndex() { return colorByPerf.getColorIndex(); }
    /**
     * Returns the text to display
     */
    public String getText() { return colorByPerf.getText(); }

    /**
     * Returns the cut points
     */
    public List<Integer> getCutpoints() { return assmts.getCutpoints(field, student); }

    /**
     * Returns the number of possible performance levels that are "real"
     */
    public Integer getNumRealPerfLevels() { 
        GenericEntity assmt = assmts.resolveAssessment(field, student);
        String dataPointId = field.getValue();
        String assmtName = dataPointId.substring(0, dataPointId.indexOf('.'));
        
        if (assmt == null) { return null; }
        return assmts.getMetaData().findNumRealPerfLevelsForFamily(assmtName);
    }
    
    /**
     * Returns the performance level
     */
    public Integer getPerfLevel() { 
        GenericEntity assmt = assmts.resolveAssessment(field, student);

        if (assmt == null) { return null; }
        int level = (Integer.parseInt((String) (assmts.getScore(assmt, Constants.ATTR_PERF_LEVEL))));
        return (new Integer(level));
    }

    /**
     * Returns the score
     */
    public Integer getScore() { 
        GenericEntity assmt = assmts.resolveAssessment(field, student);
        return assmt == null ? null : (Integer.parseInt((String) (assmts.getScore(assmt, Constants.ATTR_SCALE_SCORE))));
    }
}

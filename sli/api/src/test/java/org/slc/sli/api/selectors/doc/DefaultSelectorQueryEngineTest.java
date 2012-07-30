package org.slc.sli.api.selectors.doc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.selectors.model.*;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.modeling.uml.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests
 *
 * @author srupasinghe
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class DefaultSelectorQueryEngineTest {

    @Autowired
    DefaultSelectorQueryEngine defaultSelectorQueryEngine;

    private ModelProvider provider;

    final static String TEST_XMI_LOC = "/sliModel/test_SLI.xmi";

    @Before
    public void setup() {
        provider = new ModelProvider(TEST_XMI_LOC);
    }

    @Test
    public void testComplexSelector() {
        SemanticSelector selectorsWithType =  generateSelectorObjectMap();
        ClassType studentType = provider.getClassType("Student");

        Map<Type, SelectorQueryPlan> queryPlan = defaultSelectorQueryEngine.assembleQueryPlan(selectorsWithType);

        assertNotNull("Should not be null", queryPlan);

        SelectorQueryPlan plan = queryPlan.get(studentType);
        assertNotNull("Should not be null", plan);
        assertNotNull("Should not be null", plan.getQuery());
        assertEquals("Should match", 2, plan.getChildQueryPlans().size());

        //NeutralQuery query = plan.getQuery();
        assertEquals("Should match", 2, plan.getIncludeFields().size());
    }

    @Test
    public void testIncludeAllSelector() {
        SemanticSelector selectorsWithType =  generateIncludeAllSelectorObjectMap();
        ClassType studentType = provider.getClassType("Student");
        ClassType studentSchoolAssocicationType = provider.getClassType("schoolAssociations<=>student");

        Map<Type, SelectorQueryPlan> queryPlan = defaultSelectorQueryEngine.assembleQueryPlan(selectorsWithType);

        assertNotNull("Should not be null", queryPlan);

        SelectorQueryPlan plan = queryPlan.get(studentType);
        assertNotNull("Should not be null", plan);
        assertNotNull("Should not be null", plan.getQuery());
        assertEquals("Should match", 1, plan.getChildQueryPlans().size());

        Map<Type, SelectorQueryPlan> childQueries = (Map<Type, SelectorQueryPlan>) plan.getChildQueryPlans().get(0);
        SelectorQueryPlan schoolAsscPlan = childQueries.get(studentSchoolAssocicationType);
        assertNotNull("Should not be null", schoolAsscPlan);
        assertNotNull("Should not be null", schoolAsscPlan.getQuery());
        assertNull("Should be null", schoolAsscPlan.getQuery().getIncludeFields());
        assertNull("Should be null", schoolAsscPlan.getQuery().getExcludeFields());
    }

    @Test
    public void testExcludeSelector() {
        //TODO
        SemanticSelector selectorsWithType =  generateExcludeSelectorObjectMap();
        ClassType studentType = provider.getClassType("Student");

        Map<Type, SelectorQueryPlan> queryPlan = defaultSelectorQueryEngine.assembleQueryPlan(selectorsWithType);

        assertNotNull("Should not be null", queryPlan);

        SelectorQueryPlan plan = queryPlan.get(studentType);
        assertNotNull("Should not be null", plan);
        assertNotNull("Should not be null", plan.getQuery());
        assertNull("Should be null", plan.getQuery().getIncludeFields());
        assertNull("Should be null", plan.getQuery().getExcludeFields());
        assertEquals("Should match", 1, plan.getChildQueryPlans().size());
    }

    @Test
    public void testAssociationSelector() {
        SemanticSelector selectorsWithType =  generateAssociationSelectorMap();
        ClassType studentType = provider.getClassType("Student");

        Map<Type, SelectorQueryPlan> queryPlan = defaultSelectorQueryEngine.assembleQueryPlan(selectorsWithType);

        assertNotNull("Should not be null", queryPlan);

        SelectorQueryPlan plan = queryPlan.get(studentType);
        assertNotNull("Should not be null", plan);
        assertNotNull("Should not be null", plan.getQuery());
        assertEquals("Should match", 1, plan.getChildQueryPlans().size());
    }

    public SemanticSelector generateAssociationSelectorMap() {
        ClassType studentType = provider.getClassType("Student");

        Attribute name = getMockAttribute("name");
        ClassType sectionAssociations = getMockClassType("StudentSectionAssociation");

        SemanticSelector studentsAttrs = new SemanticSelector();
        List<SelectorElement> attributes1 = new ArrayList<SelectorElement>();
        attributes1.add(new BooleanSelectorElement(name, true));
        attributes1.add(new BooleanSelectorElement(sectionAssociations, true));
        studentsAttrs.put(studentType, attributes1);

        return studentsAttrs;
    }

    public SemanticSelector generateIncludeAllSelectorObjectMap() {
        ClassType studentType = provider.getClassType("Student");
        ClassType studentSchoolAssocicationType = provider.getClassType("schoolAssociations<=>student");

        Attribute name = getMockAttribute("name");
        Attribute economicDisadvantaged = getMockAttribute("economicDisadvantaged");

        SemanticSelector studentsAttrs = new SemanticSelector();
        List<SelectorElement> attributes1 = new ArrayList<SelectorElement>();
        attributes1.add(new BooleanSelectorElement(name, true));
        attributes1.add(new BooleanSelectorElement(economicDisadvantaged, true));
        attributes1.add(new IncludeAllSelectorElement(studentSchoolAssocicationType));
        studentsAttrs.put(studentType, attributes1);

        return studentsAttrs;
    }

    public SemanticSelector generateExcludeSelectorObjectMap() {
        ClassType studentType = provider.getClassType("Student");

        Attribute name = getMockAttribute("name");

        SemanticSelector studentsAttrs = new SemanticSelector();
        List<SelectorElement> attributes1 = new ArrayList<SelectorElement>();
        attributes1.add(new BooleanSelectorElement(name, false));
        attributes1.add(new IncludeAllSelectorElement(studentType));
        studentsAttrs.put(studentType, attributes1);

        return studentsAttrs;
    }

    public SemanticSelector generateSelectorObjectMap() {
        ClassType sectionType = provider.getClassType("Section");
        ClassType studentType = provider.getClassType("Student");
        ClassType studentSchoolAssocicationType = provider.getClassType("schoolAssociations<=>student");
        ClassType studentSectionAssocicationType = provider.getClassType("sectionAssociations<=>student");

        Attribute entryGradeLevel = getMockAttribute("entryGradeLevel");
        Attribute entryDate = getMockAttribute("entryDate");
        Attribute someField = getMockAttribute("someField");
        Attribute sessionId = getMockAttribute("sessionId");
        Attribute name = getMockAttribute("name");
        Attribute economicDisadvantaged = getMockAttribute("economicDisadvantaged");

        SemanticSelector studentSchoolAttrs = new SemanticSelector();
        List<SelectorElement> attributes = new ArrayList<SelectorElement>();
        attributes.add(new BooleanSelectorElement(entryGradeLevel, true));
        attributes.add(new BooleanSelectorElement(entryDate, true));
        studentSchoolAttrs.put(studentSchoolAssocicationType, attributes);

        SemanticSelector sectionAttrs = new SemanticSelector();
        List<SelectorElement> attributes3 = new ArrayList<SelectorElement>();
        attributes3.add(new BooleanSelectorElement(sessionId, true));
        sectionAttrs.put(sectionType, attributes3);

        SemanticSelector studentSectionAttrs = new SemanticSelector();
        List<SelectorElement> attributes2 = new ArrayList<SelectorElement>();
        attributes2.add(new BooleanSelectorElement(someField, true));
        attributes2.add(new ComplexSelectorElement(sectionType, sectionAttrs));
        studentSectionAttrs.put(studentSectionAssocicationType, attributes2);

        SemanticSelector studentsAttrs = new SemanticSelector();
        List<SelectorElement> attributes1 = new ArrayList<SelectorElement>();
        attributes1.add(new BooleanSelectorElement(name, true));
        attributes1.add(new BooleanSelectorElement(economicDisadvantaged, true));
        attributes1.add(new ComplexSelectorElement(studentSchoolAssocicationType, studentSchoolAttrs));
        attributes1.add(new ComplexSelectorElement(studentSectionAssocicationType, studentSectionAttrs));
        studentsAttrs.put(studentType, attributes1);

        return studentsAttrs;
    }

    private ClassType getMockClassType(String typeName) {
        final Multiplicity multiplicity = new Multiplicity(new Range(Occurs.ZERO, Occurs.ONE));
        final AssociationEnd end1 = new AssociationEnd(multiplicity, "end1", true, Identifier.random());
        final AssociationEnd end2 = new AssociationEnd(multiplicity, "end2", true, Identifier.random());

        return new ClassType(typeName, end1, end2);
    }

    private Attribute getMockAttribute(String attributeName) {
        final Multiplicity multiplicity = new Multiplicity(new Range(Occurs.ZERO, Occurs.ONE));

        return new Attribute(Identifier.random(), attributeName, Identifier.random(),
                multiplicity, new ArrayList<TaggedValue>());
    }
}

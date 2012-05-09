package org.slc.sli.api.security.context.traversal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slc.sli.api.security.context.resolver.EdOrgToChildEdOrgNodeFilter;
import org.slc.sli.api.security.context.traversal.graph.NodeFilter;
import org.slc.sli.api.security.context.traversal.graph.SecurityNode;
import org.slc.sli.api.security.context.traversal.graph.SecurityNodeBuilder;
import org.slc.sli.api.security.context.traversal.graph.SecurityNodeConnection;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.common.constants.ResourceNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Basic brute force path finding implementation.
 */
@Component
public class BrutePathFinder implements SecurityPathFinder {
    private Map<String, SecurityNode> nodeMap;
    private Map<String, List<SecurityNode>> prePath;
    private List<String> excludePath;

    @Autowired
    private EdOrgToChildEdOrgNodeFilter edorgFilter;

    @Autowired
    private NodeFilter sectionGracePeriodNodeFilter;

    @Autowired
    private NodeFilter studentGracePeriodNodeFilter;

    @PostConstruct
    public void init() {
        nodeMap = new HashMap<String, SecurityNode>();
        prePath = new HashMap<String, List<SecurityNode>>();
        excludePath = new ArrayList<String>();

        nodeMap.put(EntityNames.TEACHER,
                SecurityNodeBuilder.buildNode(EntityNames.TEACHER, EntityNames.STAFF)
                        .addConnection(EntityNames.SECTION, "sectionId", ResourceNames.TEACHER_SECTION_ASSOCIATIONS,
                                sectionGracePeriodNodeFilter)
                        .addConnection(EntityNames.SCHOOL, "schoolId", ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS,
                                edorgFilter)

                        .construct());
        nodeMap.put(
                EntityNames.SCHOOL,
                SecurityNodeBuilder.buildNode(EntityNames.SCHOOL, EntityNames.EDUCATION_ORGANIZATION)
                        .addConnection(EntityNames.TEACHER, "teacherId", ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS)
                        .addConnection(EntityNames.STAFF, "staffReference",
                                ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS)
                        .construct());

        nodeMap.put(EntityNames.SECTION,
                SecurityNodeBuilder.buildNode(EntityNames.SECTION)
                        .addConnection(EntityNames.TEACHER, "teacherId", ResourceNames.TEACHER_SECTION_ASSOCIATIONS)
                        .addConnection(EntityNames.STUDENT, "studentId", ResourceNames.STUDENT_SECTION_ASSOCIATIONS)
                        .addConnection(EntityNames.COURSE, "courseId", EntityNames.SECTION)
                        .addConnection(EntityNames.SESSION, "sessionId", EntityNames.SECTION)
                        .addConnection(EntityNames.PROGRAM, "programReference", "")
                        .construct());

        nodeMap.put(EntityNames.STUDENT,
                SecurityNodeBuilder.buildNode(EntityNames.STUDENT)
                        .addConnection(EntityNames.SECTION, "sectionId", ResourceNames.STUDENT_SECTION_ASSOCIATIONS)
                        .addConnection(EntityNames.ASSESSMENT, "assessmentId", ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS)
                        .addConnection(EntityNames.ATTENDANCE, "studentId", "")
                        .addConnection(EntityNames.DISCIPLINE_ACTION, "studentId", "")
                        .addConnection(EntityNames.DISCIPLINE_INCIDENT, "disciplineIncidentId",
                                ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS)
                        .addConnection(EntityNames.PARENT, "parentId", ResourceNames.STUDENT_PARENT_ASSOCIATIONS)
                        .construct());

        // Leaf Nodes are unconnected
        nodeMap.put(EntityNames.ATTENDANCE, SecurityNodeBuilder.buildNode(EntityNames.ATTENDANCE).construct());

        nodeMap.put(EntityNames.PROGRAM,
                SecurityNodeBuilder.buildNode(EntityNames.PROGRAM)
                        .construct());

        nodeMap.put(EntityNames.COURSE, SecurityNodeBuilder.buildNode(EntityNames.COURSE).construct());
        nodeMap.put(EntityNames.SESSION, SecurityNodeBuilder.buildNode(EntityNames.SESSION).construct());

        nodeMap.put(EntityNames.COHORT,
                SecurityNodeBuilder.buildNode(EntityNames.COHORT)
                        .construct());

        nodeMap.put(EntityNames.ASSESSMENT, SecurityNodeBuilder.buildNode(EntityNames.ASSESSMENT).construct());

        nodeMap.put(EntityNames.DISCIPLINE_ACTION,
                SecurityNodeBuilder.buildNode(EntityNames.DISCIPLINE_ACTION)
                        .construct());

        nodeMap.put(EntityNames.DISCIPLINE_INCIDENT,
                SecurityNodeBuilder.buildNode(EntityNames.DISCIPLINE_INCIDENT)
                        .construct());

        nodeMap.put(EntityNames.PARENT, SecurityNodeBuilder.buildNode(EntityNames.PARENT).construct());

        // excludePath.add(EntityNames.TEACHER + EntityNames.SECTION);
        excludePath.add(EntityNames.TEACHER + EntityNames.EDUCATION_ORGANIZATION);
        excludePath.add(EntityNames.TEACHER + EntityNames.COHORT);
        excludePath.add(EntityNames.TEACHER + EntityNames.PROGRAM);
        excludePath.add(EntityNames.STAFF + EntityNames.COHORT);
        excludePath.add(EntityNames.STAFF + EntityNames.PROGRAM);
        excludePath.add(EntityNames.STAFF + EntityNames.DISCIPLINE_INCIDENT);
        excludePath.add(EntityNames.STAFF + EntityNames.DISCIPLINE_ACTION);

        nodeMap.put(EntityNames.STAFF,
                SecurityNodeBuilder.buildNode(EntityNames.STAFF)
                        .addConnection(EntityNames.EDUCATION_ORGANIZATION, "educationOrganizationReference",
                                ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS, edorgFilter)
                        .construct());

        nodeMap.put(EntityNames.EDUCATION_ORGANIZATION,
                SecurityNodeBuilder.buildNode(EntityNames.EDUCATION_ORGANIZATION)
                        .addConnection(EntityNames.STAFF, "staffReference", ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS)
                        .addConnection(EntityNames.STUDENT, "studentId", ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS, studentGracePeriodNodeFilter)
                        .addConnection(EntityNames.SCHOOL, "", "")
                        .addConnection(EntityNames.PROGRAM, "programReference", "") //TODO: fix XSD
                        .addConnection(EntityNames.SECTION, "schoolId", "")
                        .construct());

        prePath.put(
                EntityNames.STAFF + EntityNames.STAFF,
                Arrays.asList(nodeMap.get(EntityNames.STAFF), nodeMap.get(EntityNames.EDUCATION_ORGANIZATION),
                        nodeMap.get(EntityNames.STAFF)));

        prePath.put(
                EntityNames.STAFF + EntityNames.SECTION,
                Arrays.asList(nodeMap.get(EntityNames.STAFF), nodeMap.get(EntityNames.EDUCATION_ORGANIZATION),
                        nodeMap.get(EntityNames.SECTION)));

        prePath.put(
                EntityNames.STAFF + EntityNames.COURSE,
                Arrays.asList(nodeMap.get(EntityNames.STAFF), nodeMap.get(EntityNames.EDUCATION_ORGANIZATION),
                        nodeMap.get(EntityNames.SECTION), nodeMap.get(EntityNames.COURSE)));

        prePath.put(
                EntityNames.STAFF + EntityNames.SESSION,
                Arrays.asList(nodeMap.get(EntityNames.STAFF), nodeMap.get(EntityNames.EDUCATION_ORGANIZATION),
                        nodeMap.get(EntityNames.SECTION), nodeMap.get(EntityNames.SESSION)));

        prePath.put(
                EntityNames.TEACHER + EntityNames.TEACHER,
                Arrays.asList(nodeMap.get(EntityNames.TEACHER), nodeMap.get(EntityNames.SCHOOL),
                        nodeMap.get(EntityNames.TEACHER)));

        prePath.put(
                EntityNames.TEACHER + EntityNames.SECTION,
                Arrays.asList(nodeMap.get(EntityNames.TEACHER), nodeMap.get(EntityNames.SECTION),
                        nodeMap.get(EntityNames.STUDENT), nodeMap.get(EntityNames.SECTION)));
        prePath.put(
                EntityNames.TEACHER + EntityNames.COURSE,
                Arrays.asList(nodeMap.get(EntityNames.TEACHER), nodeMap.get(EntityNames.SECTION),
                        nodeMap.get(EntityNames.STUDENT), nodeMap.get(EntityNames.SECTION),
                        nodeMap.get(EntityNames.COURSE)));
        prePath.put(
                EntityNames.TEACHER + EntityNames.SESSION,
                Arrays.asList(nodeMap.get(EntityNames.TEACHER), nodeMap.get(EntityNames.SECTION),
                        nodeMap.get(EntityNames.STUDENT), nodeMap.get(EntityNames.SECTION),
                        nodeMap.get(EntityNames.SESSION)));

    }

    @Override
    public List<SecurityNode> find(String from, String to) {
        return find(from, to, new ArrayList<SecurityNode>());
            }

    public List<SecurityNode> find(String from, String to, List<SecurityNode> path) {
        SecurityNode current = nodeMap.get(from);
        path.add(current);

        if (from.equals(to)) {
            return path;
                }

        for (SecurityNodeConnection connection : current.getConnections()) {
            SecurityNode next = nodeMap.get(connection.getConnectionTo());
            if (path.contains(next)) {  //cycle
                continue;
            }
            List<SecurityNode> newPath = find(next.getName(), to, path);
            if (newPath != null) {
                return newPath;
            }
        }
        debug("NO PATH FOUND FROM {} to {}", new String[] {from, to});
        path.remove(current);
        return null;
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * org.slc.sli.api.security.context.traversal.SecurityPathFinder#getPreDefinedPath(java.lang
     * .String, java.lang.String)
     */
    @Override
    public List<SecurityNode> getPreDefinedPath(String from, String to) {
        if (prePath.containsKey(from + to)) {
            return prePath.get(from + to);
        }
        return new ArrayList<SecurityNode>();
    }

    private boolean checkForFinalNode(String to, SecurityNode temp) {
        return temp.getName().equals(to);
    }

    public void setNodeMap(Map<String, SecurityNode> nodeMap) {
        this.nodeMap = nodeMap;
    }

    /**
     * @return the nodeMap
     */
    public Map<String, SecurityNode> getNodeMap() {
        return nodeMap;
    }

    public boolean isPathExcluded(String from, String to) {
        return excludePath.contains(from + to);
    }


}

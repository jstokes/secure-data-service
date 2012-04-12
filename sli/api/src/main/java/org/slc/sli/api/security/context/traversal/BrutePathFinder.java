package org.slc.sli.api.security.context.traversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;

import org.slc.sli.api.security.context.traversal.graph.Node;
import org.slc.sli.api.security.context.traversal.graph.NodeBuilder;
import org.springframework.stereotype.Component;

/**
 * Basic brute force path finding implementation.
 */
@Component
public class BrutePathFinder implements SecurityPathFinder {
    private Map<String, Node> nodeMap;

    @PostConstruct
    public void init() {
        nodeMap = new HashMap<String, Node>();
        nodeMap.put("teacher", NodeBuilder.buildNode("teacher").addConnection("section", "sectionId", null).construct());
        nodeMap.put("section", NodeBuilder.buildNode("section").addConnection("teacher", "teacherId", null).
                addConnection("student", "studentId", "sectionStudentAssociation").construct());
        nodeMap.put("student", NodeBuilder.buildNode("student").addConnection("section", "sectionId", null).construct());
    }

    @Override
    public List<Node> find(String from, String to) {
        Stack<Node> exploring = new Stack<Node>();
        List<Node> explored = new ArrayList<Node>();
        Node temp = null;
        temp = nodeMap.get(from);
        exploring.push(temp);
        debug("Starting with node: {}", temp.getName());
        while (!exploring.empty()) {
            temp = exploring.pop();
            explored.add(temp);
            debug("Marking {} as explored", temp.getName());
            if (checkForFinalNode(to, temp)) {
                debug("Returning a path of size {}", explored.size());
                return explored;
            }
            for (Map<String, String> connection : temp.getConnections()) {
                if (!explored.contains(nodeMap.get(connection.get("entity")))) {
                    debug("Enqueuing: {}", connection.get("entity"));
                    exploring.push(nodeMap.get(connection.get("entity")));
                }
            }
        }
        debug("NO PATH FOUND FROM {} to {}", new String[] {from, to});
        return null;
    }

    private boolean checkForFinalNode(String to, Node temp) {
        return temp.getName().equals(to);
    }

    public void setNodeMap(Map<String, Node> nodeMap) {
        this.nodeMap = nodeMap;
    }
}

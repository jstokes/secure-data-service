package org.slc.sli.api.ldap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Define the class that represents the user entry in ldap directory
 *
 * @author dliu
 *
 */
public class User {

    private String uid;
    private List<String> groups;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String tenant;
    private String edorg;
    private String homeDir;
    private String fullName;
    private String cn;
    
    public String getCn() {
        return cn;
    }
    
    public void setCn(String cn) {
        this.cn = cn;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private Date createTime;
    private Date modifyTime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void addGroup(String group) {
        if (groups == null) {
            groups = new ArrayList<String>();
        }
        groups.add(group);
    }

    public void removeGroup(String group) {
        if (groups != null && groups.contains(group)) {
            groups.remove(group);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getEdorg() {
        return edorg;
    }

    public void setEdorg(String edorg) {
        this.edorg = edorg;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public void setFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        if (firstName != null && lastName != null && !firstName.equals("") && !lastName.equals("")) {
            return this.firstName + " " + this.lastName;
        } else {
            return "";
        }
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateTime() {
        if (createTime == null) {
            return null;
        }
        return toDateString(createTime);
    }

    public String getModifyTime() {
        if (modifyTime == null) {
            return null;
        }
        return toDateString(modifyTime);
    }

    private String toDateString(Date date) {
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        return simpleDataFormat.format(date);
    }

    public static String printGroup(List<String> groups) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                sb.append(groups.get(i));
                if (i != groups.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("uid = ").append(uid).append("\n");
        sb.append("first name = ").append(firstName).append("\n");
        sb.append("last name = ").append(lastName).append("\n");
        sb.append("groups = ").append(printGroup(groups)).append("\n");
        sb.append("email = ").append(email).append("\n");
        sb.append("tenant = ").append(tenant).append("\n");
        sb.append("edorg = ").append(edorg).append("\n");
        sb.append("home dir = ").append(homeDir).append("\n");
        sb.append("create time = ").append(getCreateTime()).append("\n");
        sb.append("modify time = ").append(getModifyTime()).append("\n");

        return sb.toString();
    }
}

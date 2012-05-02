package org.slc.sli.entity;

/**
 * 
 * CustomConfigString
 *TODO: Add Javadoc
 */
public class CustomConfigString {
    String configString;

    CustomConfigString() {

    }

    public CustomConfigString(String customConfigString) {
        this.configString = customConfigString;
    }

    public CustomConfigString(CustomConfig customConfig) {
        this.configString = customConfig.toJson();
    }

    public String getConfigString() {
        return this.configString;
    }
}

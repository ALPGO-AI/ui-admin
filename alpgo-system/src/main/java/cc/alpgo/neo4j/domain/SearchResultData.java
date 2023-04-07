package cc.alpgo.neo4j.domain;


import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class SearchResultData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String patterns;
    private String outputs;

    public String getPatterns() {
        return patterns;
    }

    public void setPatterns(String patterns) {
        this.patterns = patterns;
    }

    public String getOutputs() {
        return outputs;
    }

    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    public String getRelations() {
        return relations;
    }

    public void setRelations(String relations) {
        this.relations = relations;
    }

    private String relations;
}

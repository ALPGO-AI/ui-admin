package cc.alpgo.neo4j.domain;


import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class SearchResultData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String nodes;

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getRelations() {
        return relations;
    }

    public void setRelations(String relations) {
        this.relations = relations;
    }
    public void setRelations(Collection<Map<String, Object>> relations) {
        this.relations = new Gson().toJson(relations);
    }

    private String relations;

    public void setNodes(Collection<Map<String, Object>> nodes) {
        this.nodes = new Gson().toJson(nodes);
    }

    public String getNodes() {
        return nodes;
    }
}

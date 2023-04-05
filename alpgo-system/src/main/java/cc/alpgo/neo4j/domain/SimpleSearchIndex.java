package cc.alpgo.neo4j.domain;

import java.io.Serializable;

public class SimpleSearchIndex implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String keyid;
    private int data_type;

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setData_type(int dataType) {
        this.data_type = dataType;
    }

    public int getData_type() {
        return data_type;
    }
}

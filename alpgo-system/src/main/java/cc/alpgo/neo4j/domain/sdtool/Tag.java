package cc.alpgo.neo4j.domain.sdtool;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("Tag")
public class Tag {

    @Id
    private String id;

    private String value;
    private String name;
    private String type;
    @Relationship(type="BELONG", direction=INCOMING)
    private Set<Tag> parentTagSet = new HashSet<>();

    public Set<Tag> getParentTagSet() {
        return parentTagSet;
    }

    public void setParentTagSet(Set<Tag> parentTagSet) {
        this.parentTagSet = parentTagSet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

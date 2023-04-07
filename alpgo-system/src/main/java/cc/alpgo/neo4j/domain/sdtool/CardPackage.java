package cc.alpgo.neo4j.domain.sdtool;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("CardPackage")
public class CardPackage {

    @Id
    private String id;
    private String name;
    private String type;
    private Integer rarity;
    @Relationship(type="PACKAGE_IN", direction=INCOMING)
    private List<Output> imageSet = new ArrayList<>();

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    public List<Output> getImageSet() {
        return imageSet;
    }

    public void setImageSet(List<Output> imageSet) {
        this.imageSet = imageSet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

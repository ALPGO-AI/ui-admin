package cc.alpgo.neo4j.domain.sdtool;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("Output")
public class Output {
    @Id
    private String id;
    private Long outputId;
    private Long patternId;
    private String outputImageUrls;

    private String prompt;
    private String negativePrompt;
    private Double height;
    private Double width;
    @Relationship(type="GENERATE_BY", direction=INCOMING)
    private Set<Pattern> patternSet = new HashSet<>();
    @Relationship(type="PROMPT_TAG", direction=OUTGOING)
    private Set<Tag> promptTagSet = new HashSet<>();
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Set<Tag> getPromptTagSet() {
        return promptTagSet;
    }

    public void setPromptTagSet(Set<Tag> promptTagSet) {
        this.promptTagSet = promptTagSet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Long getOutputId() {
        return outputId;
    }

    public void setOutputId(Long outputId) {
        this.outputId = outputId;
    }

    public Long getPatternId() {
        return patternId;
    }

    public void setPatternId(Long patternId) {
        this.patternId = patternId;
    }

    public String getOutputImageUrls() {
        return outputImageUrls;
    }

    public void setOutputImageUrls(String outputImageUrls) {
        this.outputImageUrls = outputImageUrls;
    }

    public Set<Pattern> getPatternSet() {
        return patternSet;
    }

    public void setPatternSet(Set<Pattern> patternSet) {
        this.patternSet = patternSet;
    }
}

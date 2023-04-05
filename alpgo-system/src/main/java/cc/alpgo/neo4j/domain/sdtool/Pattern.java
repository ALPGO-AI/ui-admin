package cc.alpgo.neo4j.domain.sdtool;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@Node("Pattern")
public class Pattern {

	@Id
	private String id;

	private Long patternId;
	private String patternStyle;
	private String prompt;
	private String model;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Long getPatternId() {
		return patternId;
	}

	public void setPatternId(Long patternId) {
		this.patternId = patternId;
	}

	public String getPatternStyle() {
		return patternStyle;
	}

	public void setPatternStyle(String patternStyle) {
		this.patternStyle = patternStyle;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}

package cc.alpgo.neo4j.repository;

import cc.alpgo.neo4j.domain.sdtool.Pattern;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PatternRepository extends Neo4jRepository<Pattern, String> {


	@Query("MATCH (pattern:Pattern) WHERE pattern.patternId = $patternId RETURN pattern")
	Pattern findByPatternId(@Param("patternId")Long patternId);
	@Query("MATCH (m:Pattern)-[r:HAS_LABEL]->(related) RETURN related.name as name, type(r) as relation")
	List<Map<String, Object>> findAllPattern();
}

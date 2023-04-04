package cc.alpgo.neo4j.repository;

import cc.alpgo.neo4j.domain.sdtool.Output;
import cc.alpgo.neo4j.domain.sdtool.Pattern;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface OutputRepository extends Neo4jRepository<Output, String> {

}

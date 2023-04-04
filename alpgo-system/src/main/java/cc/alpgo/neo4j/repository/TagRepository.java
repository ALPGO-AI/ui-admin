package cc.alpgo.neo4j.repository;

import cc.alpgo.neo4j.domain.sdtool.Output;
import cc.alpgo.neo4j.domain.sdtool.Tag;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TagRepository extends Neo4jRepository<Tag, String> {

}

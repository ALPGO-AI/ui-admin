package cc.alpgo.neo4j.repository;

import cc.alpgo.neo4j.domain.sdtool.CardPackage;
import cc.alpgo.neo4j.domain.sdtool.Tag;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CardPackageRepository extends Neo4jRepository<CardPackage, String> {

}

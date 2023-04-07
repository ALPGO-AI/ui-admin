package cc.alpgo.neo4j.service;

import cc.alpgo.neo4j.domain.SearchResultData;
import cc.alpgo.neo4j.domain.sdtool.CardPackage;
import cc.alpgo.neo4j.domain.sdtool.Output;
import cc.alpgo.sdtool.domain.PackageCardRequestBody;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import org.neo4j.driver.Result;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface INeo4jService {
    void createOutput(StableDiffusionOutput sdOutput) throws Exception;
    SearchResultData fetchPatternGraph();
    CardPackage packageCard(PackageCardRequestBody packageCardRequestBody);
}

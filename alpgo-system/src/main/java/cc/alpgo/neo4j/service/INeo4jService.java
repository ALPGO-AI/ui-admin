package cc.alpgo.neo4j.service;

import cc.alpgo.sdtool.domain.StableDiffusionOutput;

import java.util.List;
import java.util.Map;

public interface INeo4jService {
    void createOutput(StableDiffusionOutput sdOutput) throws Exception;
    List<Map<String, Object>> fetchPatternGraph();
}

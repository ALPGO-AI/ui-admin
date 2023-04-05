package cc.alpgo.neo4j.controller;

import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.neo4j.service.INeo4jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael J. Simons
 */
@RestController
class Neo4jController {

    @Autowired
    private INeo4jService patternService;

    @GetMapping("/graph")
    public AjaxResult getGraph() {
        return AjaxResult.success(patternService.fetchPatternGraph());
    }
}
package cc.alpgo.sdtool.controller;

import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.neo4j.service.INeo4jService;
import cc.alpgo.sdtool.util.AIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sdtool/card")
public class CardController {
    @Autowired
    private INeo4jService neo4jService;

    @Autowired
    private AIUtil aiUtil;

    @GetMapping("/open/{type}/{content}")
    public AjaxResult aiToolOpenApi(@PathVariable("content") String content, @PathVariable("type") String type) throws IOException {

        return AjaxResult.success(aiUtil.cartToolCall(type, content));
    }
    @GetMapping("/graph")
    public AjaxResult graph(){

        return AjaxResult.success(neo4jService.fetchPatternGraph());
    }
}

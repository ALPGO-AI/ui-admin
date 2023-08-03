package cc.alpgo.sdtool.controller;

import cc.alpgo.common.core.domain.AjaxResult;
import cc.alpgo.neo4j.service.INeo4jService;
import cc.alpgo.sdtool.domain.OpenAIRequest;
import cc.alpgo.sdtool.util.AIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/open")
    public AjaxResult aiToolOpenApiPostCustom(@RequestBody OpenAIRequest req) throws IOException {

        return AjaxResult.success(aiUtil.cartToolCall(req));
    }
    @GetMapping("/graph")
    public AjaxResult graph(){

        return AjaxResult.success(neo4jService.fetchPatternGraph());
    }
}

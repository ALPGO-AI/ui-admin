package cc.alpgo.neo4j.service.impl;

import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.neo4j.domain.sdtool.Output;
import cc.alpgo.neo4j.domain.sdtool.Pattern;
import cc.alpgo.neo4j.domain.sdtool.Tag;
import cc.alpgo.neo4j.repository.OutputRepository;
import cc.alpgo.neo4j.repository.PatternRepository;
import cc.alpgo.neo4j.service.INeo4jService;
import cc.alpgo.neo4j.utils.IdUtil;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

@Service
public class Neo4jServiceImpl implements INeo4jService {

    @Autowired
    private PatternRepository patternRepository;
    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;

    @Override
    public void createOutput(StableDiffusionOutput sdOutput) throws Exception {
        Long patternId = sdOutput.getPatternId();
        Pattern patternNodeOptional = patternRepository.findByPatternId(patternId);
        Pattern pattern = null;
        if (patternNodeOptional == null) {
            StableDiffusionPattern stableDiffusionPattern = stableDiffusionPatternService.selectStableDiffusionPatternByPatternId(patternId);
            pattern = convertFromSdPattern(stableDiffusionPattern);
            pattern = patternRepository.save(pattern);
        } else {
            pattern = patternNodeOptional;
        }

        Gson gson = new Gson();
        // generatePromptTag
        StableDiffusionApiResponse res = gson.fromJson(sdOutput.getStraightParameter(), StableDiffusionApiResponse.class);
        List<Object> params = res.getData();
        Object paramStringObj = params.get(1);
        String paramString = (String) paramStringObj;
        HashMap<String, Object> hashMap = gson.fromJson(paramString, HashMap.class);

        String prompt = (String) hashMap.get("prompt");
        String negative_prompt = (String) hashMap.get("negative_prompt");
        Double height = (Double) hashMap.get("height");
        Double width = (Double) hashMap.get("width");

        Set<Pattern> patternSet = new HashSet<>();
        patternSet.add(pattern);
        Output output = convertFromSdOutput(sdOutput, prompt, negative_prompt, height, width);
        output.setPatternSet(patternSet);

        Set<Tag> promptTags = convertToTags(prompt, "prompt");
        output.setPromptTagSet(promptTags);
        outputRepository.save(output);
    }

    private Set<Tag> convertToTags(String arrString, String type) {
        Set<Tag> tags = new HashSet<>();
        String[] split = arrString.split(",");
        if (split == null || split.length < 1) {
            return new HashSet<>();
        }
        for (String sTag : split) {
            Tag tag = new Tag();
            if (StringUtils.isEmpty(sTag)) {
                continue;
            }
            tag.setId(IdUtil.generateTagId(sTag));
            tag.setName(sTag.trim());
            tag.setType(type);
            tag.setValue(sTag.trim());
            tags.add(tag);
        }
        return tags;
    }


    @Override
    public List<Map<String, Object>> fetchPatternGraph() {
        List<Map<String, Object>> allPatterns = patternRepository.findAllPattern();
        if (isEmpty(allPatterns)) {
            return new ArrayList<>();
        }
        return allPatterns;
    }

    private Output convertFromSdOutput(StableDiffusionOutput sdOutput, String prompt, String negative_prompt, Double height, Double width) {
        Output output = new Output();
        output.setId(IdUtil.generateId(sdOutput));
        output.setOutputId(sdOutput.getOutputId());
        output.setPatternId(sdOutput.getPatternId());
        output.setOutputImageUrls(sdOutput.getOutputImageUrl());
        output.setPrompt(prompt);
        output.setNegativePrompt(negative_prompt);
        output.setHeight(height);
        output.setWidth(width);
        return output;
    }
    private Pattern convertFromSdPattern(StableDiffusionPattern sdPattern) {
        Pattern patternNode = new Pattern();
        patternNode.setId("PATTERN_" + sdPattern.getPatternId());
        patternNode.setPatternId(sdPattern.getPatternId());
        patternNode.setModel(sdPattern.getModel());
        patternNode.setPrompt(sdPattern.getPositivePrompt());
        patternNode.setPatternStyle(sdPattern.getPatternStyle());
        return patternNode;
    }

}

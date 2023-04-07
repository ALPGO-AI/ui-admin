package cc.alpgo.neo4j.service.impl;

import cc.alpgo.common.utils.StringUtils;
import cc.alpgo.neo4j.domain.SearchResultData;
import cc.alpgo.neo4j.domain.sdtool.CardPackage;
import cc.alpgo.neo4j.domain.sdtool.Output;
import cc.alpgo.neo4j.domain.sdtool.Pattern;
import cc.alpgo.neo4j.domain.sdtool.Tag;
import cc.alpgo.neo4j.repository.CardPackageRepository;
import cc.alpgo.neo4j.repository.OutputRepository;
import cc.alpgo.neo4j.repository.PatternRepository;
import cc.alpgo.neo4j.service.INeo4jService;
import cc.alpgo.neo4j.utils.IdUtil;
import cc.alpgo.sdtool.domain.PackageCardRequestBody;
import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import cc.alpgo.sdtool.util.res.StableDiffusionApiResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Neo4jServiceImpl implements INeo4jService {
    private static final Logger log = LoggerFactory.getLogger(Neo4jServiceImpl.class);

    @Autowired
    private PatternRepository patternRepository;
    @Autowired
    private OutputRepository outputRepository;
    @Autowired
    private CardPackageRepository cardPackageRepository;

    @Autowired
    private IStableDiffusionPatternService stableDiffusionPatternService;
    @Autowired
    private Neo4jClient client;

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
    public SearchResultData fetchPatternGraph() {
        SearchResultData searchResultData = new SearchResultData();
        try {
            Gson gson = new Gson();
            Neo4jClient.RunnableSpec fetchPattern = client.query("match (n:Pattern) return n;");
            searchResultData.setPatterns(gson.toJson(fetchPattern.fetch().all()));
            Neo4jClient.RunnableSpec fetchOutput = client.query("match (n:Output) WHERE NOT EXISTS {MATCH (n:Output)-[r2:PACKAGE_IN]-(n2:CardPackage)} return n;");
            searchResultData.setOutputs(gson.toJson(fetchOutput.fetch().all()));
            Neo4jClient.RunnableSpec fetchRelation = client.query("MATCH (n:Pattern)\n" +
                    "OPTIONAL MATCH (n:Pattern)-[r:GENERATE_BY]-(o:Output) \n" +
                    "RETURN r");
            Collection<Map<String, Object>> resultRelation = fetchRelation.fetch().all();
            searchResultData.setRelations(gson.toJson(resultRelation));
        }
        catch(Exception e){
            log.error("post search neo4j  error, error msg is "+ e.getMessage());
        }
        return searchResultData;
    }

    @Override
    public CardPackage packageCard(PackageCardRequestBody packageCardRequestBody) {
        CardPackage cardPackage = new CardPackage();
        cardPackage.setId(IdUtil.generateCardPackageId());
        cardPackage.setName(packageCardRequestBody.getName());
        cardPackage.setType(packageCardRequestBody.getType());
        cardPackage.setRarity(packageCardRequestBody.getRarity());
        List<Output> allById = outputRepository.findAllById(packageCardRequestBody.getOutputIds());
        cardPackage.setImageSet(allById);
        cardPackageRepository.save(cardPackage);
        return cardPackage;
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

package cc.alpgo.neo4j.utils;

import cc.alpgo.sdtool.domain.StableDiffusionOutput;
import cc.alpgo.sdtool.domain.StableDiffusionPattern;

import static cc.alpgo.common.utils.sign.Md5Utils.hash;

public class IdUtil {
    public static String generateId(StableDiffusionOutput sdOutput) {
        return "OUTPUT_" + sdOutput.getOutputId();
    }
    public static String generateId(StableDiffusionPattern pattern) {
        return "PATTERN_" + pattern.getPatternId();
    }

    public static String generateTagId(String sTag) {
        return hash(sTag.trim());
    }
}

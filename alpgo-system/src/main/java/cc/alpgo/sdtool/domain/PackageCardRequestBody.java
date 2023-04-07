package cc.alpgo.sdtool.domain;

import java.util.List;

public class PackageCardRequestBody {
    private String name;
    private String type;
    private List<String> outputIds;
    private Integer rarity;

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOutputIds() {
        return outputIds;
    }

    public void setOutputIds(List<String> outputIds) {
        this.outputIds = outputIds;
    }
}

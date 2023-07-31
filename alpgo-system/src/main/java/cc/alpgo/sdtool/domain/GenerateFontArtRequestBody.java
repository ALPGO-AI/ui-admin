package cc.alpgo.sdtool.domain;

import lombok.Data;

@Data
public class GenerateFontArtRequestBody {
    private String fontArtText;
    private Integer fontArtSize;
    private Integer width;
    private Integer height;
    private String prompt;
}

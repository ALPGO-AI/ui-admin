package cc.alpgo.sdtool.domain;

import lombok.Data;

@Data
public class OpenAIRequest {
    private String question;
    private String content;
}

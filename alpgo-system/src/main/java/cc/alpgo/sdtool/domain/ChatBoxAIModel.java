package cc.alpgo.sdtool.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChatBoxAIModel {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<ChatBoxAIModelChoice> choices;
    private String finish_reason;
}

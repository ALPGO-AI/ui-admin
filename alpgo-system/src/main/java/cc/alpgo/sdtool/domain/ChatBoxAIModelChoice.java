package cc.alpgo.sdtool.domain;

import lombok.Data;

@Data
public class ChatBoxAIModelChoice {
//        "index": 0,
//            "delta": {
//        "content": "s"
//    },
    private Integer index;
    private ChatBoxAIModelChoiceDelta delta;
}

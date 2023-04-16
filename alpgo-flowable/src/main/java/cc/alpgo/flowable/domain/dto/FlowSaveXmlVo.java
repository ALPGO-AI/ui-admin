package cc.alpgo.flowable.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tony
 * @date 2021/3/28 19:48
 */
@Data
public class FlowSaveXmlVo implements Serializable {

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程分类
     */
    private String category;

    /**
     * xml 文件
     */
    private String xml;
}

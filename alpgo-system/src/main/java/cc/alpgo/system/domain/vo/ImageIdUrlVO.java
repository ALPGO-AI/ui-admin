package cc.alpgo.system.domain.vo;

import cc.alpgo.common.annotation.Excel;

public class ImageIdUrlVO {
    private static final long serialVersionUID = 1L;
    private Long imageId;
    private String url;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

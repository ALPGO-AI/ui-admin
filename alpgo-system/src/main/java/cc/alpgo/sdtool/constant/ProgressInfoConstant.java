package cc.alpgo.sdtool.constant;

public enum ProgressInfoConstant {
    START("开始执行流程"),
    SEND_CONTROL_NET("开始处理ControlNet"),
    SEND_GENERATE("开始生成图片"),
    RECEIVE_IMAGE("正在返回图片"),
    UPLOAD_TO_COS("开始上传图片到图床"),
    DONE("流程执行完成");

    private final String msg;

    public String getMsg() {
        return msg;
    }

    ProgressInfoConstant(String msg) {
        this.msg = msg;
    }
}

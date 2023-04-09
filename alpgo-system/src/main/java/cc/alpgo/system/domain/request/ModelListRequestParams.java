package cc.alpgo.system.domain.request;

import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.common.utils.uuid.UUID;

public class ModelListRequestParams {
    private StableDiffusionEnv activeEnv;

    public ModelListRequestParams(StableDiffusionEnv activeEnv) {
        this.activeEnv = activeEnv;
    }
    public String toModelsPreDict(){
        return "{\"fn_index\":0,\"data\":[],\"session_hash\":\""+UUID.randomUUID().toString()+"\"}";
    }

    public String toControlNetModelPreDict(){
        return "{\"fn_index\":" + activeEnv.getFetchControlNetModelFnIndex() + ",\"data\":[\"None\"],\"session_hash\":\""+UUID.randomUUID().toString()+"\"}";
    }

}

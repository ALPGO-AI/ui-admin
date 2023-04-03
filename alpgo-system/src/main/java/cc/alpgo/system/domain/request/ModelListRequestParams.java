package cc.alpgo.system.domain.request;

import cc.alpgo.common.utils.StableDiffusionEnv;
import cc.alpgo.common.utils.uuid.UUID;
import com.google.gson.Gson;

public class ModelListRequestParams {

    public ModelListRequestParams(StableDiffusionEnv activeEnv) {
    }
    public String toPreDict(){
        return "{\"fn_index\":0,\"data\":[],\"session_hash\":\""+UUID.randomUUID().toString()+"\"}";
    }

}

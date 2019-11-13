package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private final String port;
    private final String memoryLimit;
    private final String cfInstanceIndex;
    private final String cfInstanceAddr;

    public EnvController(@Value("${port:NOT SET}") String PORT,
                         @Value("${memory.limit:NOT SET}") String MEMORY_LIMIT,
                         @Value("${cf.instance.index:NOT SET}") String CF_INSTANCE_INDEX,
                         @Value("${cf.instance.addr:NOT SET}") String CF_INSTANCE_ADDR) {
        this.port = PORT;
        this.memoryLimit = MEMORY_LIMIT;
        this.cfInstanceIndex = CF_INSTANCE_INDEX;
        this.cfInstanceAddr = CF_INSTANCE_ADDR;
    }

    @GetMapping("/env")
    public Map<String,String> getEnv() {
        Map<String,String> map = new HashMap<>(4);
        map.put("PORT",port);
        map.put("MEMORY_LIMIT",memoryLimit);
        map.put("CF_INSTANCE_INDEX",cfInstanceIndex);
        map.put("CF_INSTANCE_ADDR",cfInstanceAddr);
        return map;
    }

}

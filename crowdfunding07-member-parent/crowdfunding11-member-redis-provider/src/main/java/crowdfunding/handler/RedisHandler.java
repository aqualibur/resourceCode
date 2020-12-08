package crowdfunding.handler;

import crowdfunding.util.ResultEntity;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author dell
 */
@RestController
public class RedisHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/set/redis/key/value/remote")
    public ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key,
                                                @RequestParam("value") String value) {
        try {
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            valueOperations.set(key, value);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    public ResultEntity<String> setRedisKeyValueRemoteWithTimeout(@RequestParam("key") String key,
                                                           @RequestParam("value") String value,
                                                           @RequestParam("time") Long time,
                                                           @RequestParam("timeunit") TimeUnit timeUnit) {
        try {
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            valueOperations.set(key, value, time, timeUnit);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/redis/value/by/key/remote")
    public ResultEntity<String> getRedisValueByKeyRemote(@RequestParam("key") String key) {
        try {
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            String value = valueOperations.get(key);
            return ResultEntity.successWithData(value);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/remove/redis/by/key/remote")
    public ResultEntity<String> removeRedisByKeyRemote(@RequestParam("key") String key) {
        try {
            stringRedisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

}

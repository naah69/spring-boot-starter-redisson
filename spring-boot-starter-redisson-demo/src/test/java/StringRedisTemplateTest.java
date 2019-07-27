import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * StringRedisTemplateTest
 *
 * @author naah
 * @date 2019-07-27 2:03 PM
 * @desc
 */
public class StringRedisTemplateTest extends TestBase {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test() {
        //获取客户端
        ValueOperations<String, String> redisClient = redisTemplate.opsForValue();

        //key
        String key = "RedisTemplateTest";

        //删除
        redisTemplate.delete(key);

        //value
        long current = System.currentTimeMillis();
        String value = current + "";

        //set
        redisClient.set(key, value);

        //校验
        Assert.assertEquals(redisClient.get(key), value);
    }
}

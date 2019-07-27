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
        String key = "redisson_test";
        redisTemplate.delete(key);
        long current = System.currentTimeMillis();
        ValueOperations<String, String> redis = redisTemplate.opsForValue();
        String value = current + "";
        redis.set(key, value);
        Assert.assertEquals(redis.get(key), value);
    }
}

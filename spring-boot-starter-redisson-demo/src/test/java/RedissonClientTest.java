import org.junit.Assert;
import org.junit.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RedissonClientTest
 *
 * @author naah
 * @date 2019-07-27 2:03 PM
 * @desc
 */
public class RedissonClientTest extends TestBase {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() {
        //key
        String key = "RedissonClientTest";

        //获取客户端
        RBucket<String> redisClient = redissonClient.getBucket(key);

        //删除
        redisClient.delete();

        //value
        long current = System.currentTimeMillis();
        String value = current + "";

        //set
        redisClient.set(value);

        //校验
        Assert.assertEquals(redisClient.get(), value);
    }
}

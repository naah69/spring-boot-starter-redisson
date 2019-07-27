import org.junit.Test;
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
    }
}

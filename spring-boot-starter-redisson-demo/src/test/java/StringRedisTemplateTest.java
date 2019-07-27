import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

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

    }
}

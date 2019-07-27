import com.naah69.core.handler.StringHandler;
import com.naah69.core.util.RedisUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * RedissonClientTest
 *
 * @author naah
 * @date 2019-07-27 2:03 PM
 * @desc
 */
public class RedisUtilsTest extends TestBase {

    @Test
    public void test() {
        //获取客户端
        StringHandler redisClient = RedisUtil.getStringHandler();

        //key
        String key = "RedisUtilsTest";

        //删除
        redisClient.remove(key);

        //value
        long current = System.currentTimeMillis();
        String value = current + "";

        //set
        redisClient.set(key, value);

        //校验
        Assert.assertEquals(redisClient.get(key), value);
    }
}

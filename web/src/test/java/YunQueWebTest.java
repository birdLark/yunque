import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import www.larkmidtable.com.YunQueWebStart;
import www.larkmidtable.com.dao.WebDao;

/**
 *
 *
 * @Date: 2022/12/24 18:22
 * @Description:
 **/
@SpringBootTest(classes = YunQueWebStart.class)
public class YunQueWebTest {

	@Autowired
	private WebDao webDao;


	@Test
	public void test() {
		webDao.getAll();
	}
}

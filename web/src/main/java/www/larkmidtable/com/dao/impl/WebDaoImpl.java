package www.larkmidtable.com.dao.impl;

import org.springframework.stereotype.Repository;
import www.larkmidtable.com.dao.WebDao;

/**
 *
 *
 * @Date: 2022/12/24 18:29
 * @Description:
 **/
@Repository
public class WebDaoImpl implements WebDao {
	@Override
	public void getAll() {
		System.out.println("getAll...");
	}
}

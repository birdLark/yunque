package www.larkmidtable.com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import www.larkmidtable.com.domain.Task;

@Mapper
public interface TaskDao extends BaseMapper<Task> {

}

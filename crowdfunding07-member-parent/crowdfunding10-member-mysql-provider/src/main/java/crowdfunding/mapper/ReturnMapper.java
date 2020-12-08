package crowdfunding.mapper;

import java.util.List;

import crowdfunding.entity.po.ReturnExample;
import crowdfunding.entity.po.ReturnPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dell
 */
@Mapper
public interface ReturnMapper {

    long countByExample(ReturnExample example);

    int deleteByExample(ReturnExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReturnPO record);

    int insertSelective(ReturnPO record);

    List<ReturnPO> selectByExample(ReturnExample example);

    ReturnPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReturnPO record, @Param("example") ReturnExample example);

    int updateByExample(@Param("record") ReturnPO record, @Param("example") ReturnExample example);

    int updateByPrimaryKeySelective(ReturnPO record);

    int updateByPrimaryKey(ReturnPO record);

    void insertReturnPOBatch(@Param("returnPOList") List<ReturnPO> returnPOList, @Param("projectId") Integer projectId);

}
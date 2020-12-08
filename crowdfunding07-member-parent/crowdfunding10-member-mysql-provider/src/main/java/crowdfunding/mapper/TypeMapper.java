package crowdfunding.mapper;

import java.util.List;

import crowdfunding.entity.po.TypeExample;
import crowdfunding.entity.po.TypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dell
 */
@Mapper
public interface TypeMapper {

    long countByExample(TypeExample example);

    int deleteByExample(TypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TypePO record);

    int insertSelective(TypePO record);

    List<TypePO> selectByExample(TypeExample example);

    TypePO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TypePO record, @Param("example") TypeExample example);

    int updateByExample(@Param("record") TypePO record, @Param("example") TypeExample example);

    int updateByPrimaryKeySelective(TypePO record);

    int updateByPrimaryKey(TypePO record);

}
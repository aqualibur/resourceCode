package crowdfunding.mapper;

import java.util.List;

import crowdfunding.entity.po.AddressExample;
import crowdfunding.entity.po.AddressPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dell
 */
@Mapper
public interface AddressMapper {

    long countByExample(AddressExample example);

    int deleteByExample(AddressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AddressPO record);

    int insertSelective(AddressPO record);

    List<AddressPO> selectByExample(AddressExample example);

    AddressPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AddressPO record, @Param("example") AddressExample example);

    int updateByExample(@Param("record") AddressPO record, @Param("example") AddressExample example);

    int updateByPrimaryKeySelective(AddressPO record);

    int updateByPrimaryKey(AddressPO record);

}
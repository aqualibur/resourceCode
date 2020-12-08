package crowdfunding.mapper;

import java.util.List;

import crowdfunding.entity.po.ProjectExample;
import crowdfunding.entity.po.ProjectPO;
import crowdfunding.entity.vo.DetailProjectVO;
import crowdfunding.entity.vo.PortalTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dell
 */
@Mapper
public interface ProjectMapper {

    long countByExample(ProjectExample example);

    int deleteByExample(ProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void insertTypeRelationship(@Param("typeIdList") List<Integer> typeIdList, @Param("projectId") Integer projectId);

    void insertTagRelationship(@Param("tagIdList")List<Integer> tagIdList, @Param("projectId") Integer projectId);

    List<PortalTypeVO> selectPortalTypeVO();

    DetailProjectVO selectDetailProjectVO(Integer projectId);

}
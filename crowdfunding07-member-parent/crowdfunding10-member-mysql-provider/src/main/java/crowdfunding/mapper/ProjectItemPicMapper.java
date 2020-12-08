package crowdfunding.mapper;

import crowdfunding.entity.po.ProjectItemPicExample;
import crowdfunding.entity.po.ProjectItemPicPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dell
 */
@Mapper
public interface ProjectItemPicMapper {

    long countByExample(ProjectItemPicExample example);

    int deleteByExample(ProjectItemPicExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectItemPicPO record);

    int insertSelective(ProjectItemPicPO record);

    List<ProjectItemPicPO> selectByExample(ProjectItemPicExample example);

    ProjectItemPicPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectItemPicPO record, @Param("example") ProjectItemPicExample example);

    int updateByExample(@Param("record") ProjectItemPicPO record, @Param("example") ProjectItemPicExample example);

    int updateByPrimaryKeySelective(ProjectItemPicPO record);

    int updateByPrimaryKey(ProjectItemPicPO record);

    void insertPicturePath(@Param("detailPicturePathList") List<String> detailPicturePathList, @Param("projectId") Integer projectId);
}
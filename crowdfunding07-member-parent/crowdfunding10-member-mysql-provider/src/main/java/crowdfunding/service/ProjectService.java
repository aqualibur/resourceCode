package crowdfunding.service;

import crowdfunding.entity.vo.DetailProjectVO;
import crowdfunding.entity.vo.PortalTypeVO;
import crowdfunding.entity.vo.ProjectVO;

import java.util.List;

/**
 * @author dell
 */
public interface ProjectService {

    void saveProject(ProjectVO projectVO, Integer memberLoginVOId);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVO(Integer projectId);

}

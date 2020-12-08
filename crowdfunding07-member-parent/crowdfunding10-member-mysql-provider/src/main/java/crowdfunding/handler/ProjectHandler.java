package crowdfunding.handler;

import crowdfunding.entity.vo.DetailProjectVO;
import crowdfunding.entity.vo.PortalTypeVO;
import crowdfunding.entity.vo.ProjectVO;
import crowdfunding.service.ProjectService;
import crowdfunding.util.ResultEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dell
 */
@RestController
public class ProjectHandler {

    @Resource
    private ProjectService projectService;

    @RequestMapping("/save/projectVO/remote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberLoginVOId") Integer memberLoginVOId) {
        try {
            projectService.saveProject(projectVO, memberLoginVOId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/portal/type/VO/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeVORemote() {
        try {
            List<PortalTypeVO> portalTypeVOList = projectService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId) {
        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}

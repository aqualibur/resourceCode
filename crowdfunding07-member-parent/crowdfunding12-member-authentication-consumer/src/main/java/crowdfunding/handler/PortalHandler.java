package crowdfunding.handler;

import crowdfunding.api.MysqlRemoteService;
import crowdfunding.constant.CrowdFundingConstant;
import crowdfunding.entity.vo.PortalTypeVO;
import crowdfunding.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dell
 */
@Controller
public class PortalHandler {

    @Resource
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/")
    public String toPortalPage(Model model) {
        ResultEntity<List<PortalTypeVO>> resultEntity = mysqlRemoteService.getPortalTypeVORemote();
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            List<PortalTypeVO> queryData = resultEntity.getQueryData();
            model.addAttribute(CrowdFundingConstant.ATTR_NAME_PORTAL_DATA, queryData);
        }
        return "portal";
    }

}

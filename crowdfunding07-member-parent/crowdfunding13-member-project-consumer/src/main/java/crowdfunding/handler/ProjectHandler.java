package crowdfunding.handler;

import crowdfunding.api.MysqlRemoteService;
import crowdfunding.config.OssProperties;
import crowdfunding.constant.CrowdFundingConstant;
import crowdfunding.entity.vo.*;
import crowdfunding.util.CrowdFundingUtil;
import crowdfunding.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author dell
 */
@Controller
public class ProjectHandler {

    @Resource
    private OssProperties ossProperties;

    @Resource
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO,
                                       MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList,
                                       HttpSession httpSession,
                                       ModelMap modelMap) throws IOException {
        boolean headerPictureEmpty = headerPicture.isEmpty();
        if (headerPictureEmpty) {
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_HEADER_PIC_IS_EMPTY);
            return "project-launch";
        }
        ResultEntity<String> headerPicResultEntity = CrowdFundingUtil.uploadFileToOss(ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                Objects.requireNonNull(headerPicture.getOriginalFilename()),
                headerPicture.getInputStream());
        String headerPicResultEntityResult = headerPicResultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(headerPicResultEntityResult)) {
            String data = headerPicResultEntity.getQueryData();
            projectVO.setHeaderPicturePath(data);
        } else {
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }
        if (detailPictureList == null || detailPictureList.size() <= 0) {
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_DETAIL_PIC_LIST_IS_EMPTY);
            return "project-launch";
        }
        List<String> detailPicturePathList = new ArrayList<>();
        for (MultipartFile multipartFile : detailPictureList) {
            if (multipartFile.isEmpty()) {
                modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_DETAIL_PIC_IS_EMPTY);
                return "project-launch";
            }
            ResultEntity<String> detailPicResultEntity = CrowdFundingUtil.uploadFileToOss(ossProperties.getEndpoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    Objects.requireNonNull(multipartFile.getOriginalFilename()),
                    multipartFile.getInputStream());
            String detailPicResultEntityResult = detailPicResultEntity.getResult();
            if (ResultEntity.SUCCESS.equals(detailPicResultEntityResult)) {
                String data = detailPicResultEntity.getQueryData();
                detailPicturePathList.add(data);
            } else {
                modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
        }
        projectVO.setDetailPicturePathList(detailPicturePathList);
        httpSession.setAttribute(CrowdFundingConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        return "redirect:http://localhost:80/project/to/return/page";
    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> createUploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        return CrowdFundingUtil.uploadFileToOss(ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                Objects.requireNonNull(returnPicture.getOriginalFilename()),
                returnPicture.getInputStream());
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession httpSession) {
        try {
            ProjectVO projectVO = (ProjectVO) httpSession.getAttribute(CrowdFundingConstant.ATTR_NAME_TEMPLE_PROJECT);
            if (projectVO == null) {
                return ResultEntity.failed(CrowdFundingConstant.MESSAGE_TEMPLATE_PROJECT_IS_MISSING);
            }
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            if (returnVOList == null || returnVOList.size() == 0) {
                returnVOList = new ArrayList<>();
                projectVO.setReturnVOList(returnVOList);
            }
            returnVOList.add(returnVO);
            httpSession.setAttribute(CrowdFundingConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/create/confirm")
    public String createConfirm(HttpSession httpSession, MemberConfirmInfoVO memberConfirmInfoVO, ModelMap modelMap) {
        ProjectVO projectVO = (ProjectVO) httpSession.getAttribute(CrowdFundingConstant.ATTR_NAME_TEMPLE_PROJECT);
        if (projectVO == null) {
            throw new RuntimeException(CrowdFundingConstant.MESSAGE_TEMPLATE_PROJECT_IS_MISSING);
        }
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        MemberLoginVO memberLoginVO = (MemberLoginVO) httpSession.getAttribute(CrowdFundingConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberLoginVOId = memberLoginVO.getId();
        ResultEntity<String> resultEntity = mysqlRemoteService.saveProjectVORemote(projectVO, memberLoginVOId);
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "project-confirm";
        }
        httpSession.removeAttribute(CrowdFundingConstant.ATTR_NAME_TEMPLE_PROJECT);
        return "redirect:http://localhost:80/project/create/success";
    }

    @RequestMapping("/get/project/detail/remote/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model) {
        ResultEntity<DetailProjectVO> resultEntity = mysqlRemoteService.getDetailProjectVORemote(projectId);
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            DetailProjectVO queryData = resultEntity.getQueryData();
            model.addAttribute("detailProjectVO", queryData);
        }
        return "project-detail";
    }

}

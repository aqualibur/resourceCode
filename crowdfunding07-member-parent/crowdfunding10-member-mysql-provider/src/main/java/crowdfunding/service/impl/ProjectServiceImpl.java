package crowdfunding.service.impl;

import crowdfunding.entity.po.MemberConfirmInfoPO;
import crowdfunding.entity.po.MemberLaunchInfoPO;
import crowdfunding.entity.po.ProjectPO;
import crowdfunding.entity.po.ReturnPO;
import crowdfunding.entity.vo.*;
import crowdfunding.mapper.*;
import crowdfunding.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dell
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectItemPicMapper projectItemPicMapper;

    @Resource
    private ReturnMapper returnMapper;

    @Resource
    private MemberConfirmInfoMapper memberConfirmInfoMapper;

    @Resource
    private MemberLaunchInfoMapper memberLaunchInfoMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveProject(ProjectVO projectVO, Integer memberLoginVOId) {
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO, projectPO);
        projectPO.setMemberid(memberLoginVOId);
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(format);
        projectPO.setStatus(0);
        projectMapper.insertSelective(projectPO);
        Integer projectId = projectPO.getId();
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectMapper.insertTypeRelationship(typeIdList, projectId);
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectMapper.insertTagRelationship(tagIdList, projectId);
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicMapper.insertPicturePath(detailPicturePathList, projectId);
        MemberLaunchInfoVO memberLaunchInfoVO = projectVO.getMemberLaunchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLaunchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberLoginVOId);
        memberLaunchInfoMapper.insert(memberLaunchInfoPO);
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOList.add(returnPO);
        }
        returnMapper.insertReturnPOBatch(returnPOList, projectId);
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberLoginVOId);
        memberConfirmInfoMapper.insert(memberConfirmInfoPO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectMapper.selectPortalTypeVO();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        DetailProjectVO detailProjectVO = projectMapper.selectDetailProjectVO(projectId);
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("众筹失败");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
        String deployDate = detailProjectVO.getDeployDate();
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(deployDate);
            long currentDateTime = currentDate.getTime();
            long dateTime = date.getTime();
            long pastDays = (currentDateTime - dateTime) / 1000 / 60 / 60 / 24;
            Integer projectVODay = detailProjectVO.getDay();
            Integer lastDays = Math.toIntExact(projectVODay - pastDays);
            detailProjectVO.setLastDays(lastDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }
}

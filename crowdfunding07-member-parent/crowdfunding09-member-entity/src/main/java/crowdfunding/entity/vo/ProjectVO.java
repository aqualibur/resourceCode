package crowdfunding.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author dell
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVO implements Serializable {

    private static final long serialVersionUID = 2L;
    private List<Integer> tagIdList;
    private List<Integer> typeIdList;
    private String projectName;
    private String projectDescription;
    private Long money;
    private Integer day;
    private String createdate;
    private String headerPicturePath;
    private List<String> detailPicturePathList;
    private MemberLaunchInfoVO memberLaunchInfoVO;
    private List<ReturnVO> returnVOList;
    private MemberConfirmInfoVO memberConfirmInfoVO;

}

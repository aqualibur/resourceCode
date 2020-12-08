package crowdfunding.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dell
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProjectVO {

    private Integer projectId;
    private String projectName;
    private String projectDescription;
    private Integer follower;
    private Integer status;
    private String statusText;
    private Long money;
    private Integer day;
    private Long supportMoney;
    private Integer completion;
    private String deployDate;
    private Integer lastDays;
    private Integer supporter;
    private String headerPicturePath;
    private List<String> detailPicturePathList;
    private List<DetailReturnVO> detailReturnVOList;

}

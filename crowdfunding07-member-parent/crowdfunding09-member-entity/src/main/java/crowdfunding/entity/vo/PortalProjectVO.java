package crowdfunding.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dell
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {

    private Integer projectId;
    private String projectName;
    private Long money;
    private String headerPicturePath;
    private String deployDate;
    private Integer completion;
    private Integer supporter;

}

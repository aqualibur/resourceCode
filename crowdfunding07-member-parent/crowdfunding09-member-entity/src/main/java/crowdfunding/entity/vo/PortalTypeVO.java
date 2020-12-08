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
public class PortalTypeVO {

    private Integer id;
    private String name;
    private String remark;
    private List<PortalProjectVO> portalProjectVOList;

}

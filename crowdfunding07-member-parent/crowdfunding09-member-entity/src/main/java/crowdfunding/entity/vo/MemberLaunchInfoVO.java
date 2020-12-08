package crowdfunding.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dell
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLaunchInfoVO implements Serializable {

    private static final long serialVersionUID = 3L;
    private String descriptionSimple;
    private String descriptionDetail;
    private String phoneNum;
    private String serviceNum;

}

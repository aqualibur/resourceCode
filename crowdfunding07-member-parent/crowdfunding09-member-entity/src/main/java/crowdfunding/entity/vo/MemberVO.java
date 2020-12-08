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
public class MemberVO {

    private String loginacct;
    private String userpswd;
    private String username;
    private String email;

}

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
public class ReturnVO implements Serializable {

    private static final long serialVersionUID = 4L;
    private Integer type;
    private Integer supportmoney;
    private String content;
    private Integer count;
    private Integer signalpurchase;
    private Integer purchase;
    private Integer freight;
    private Integer invoice;
    private Integer returndate;
    private String describPicPath;

}

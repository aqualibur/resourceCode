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
public class DetailReturnVO {

    private Integer returnId;
    private Integer supportMoney;
    private Integer count;
    private Integer supporterCount;
    private Integer freight;
    private Integer returnDate;
    private String content;

}

package crowdfunding.test;

import crowdfunding.MysqlMain;
import crowdfunding.entity.po.MemberPO;
import crowdfunding.entity.vo.DetailProjectVO;
import crowdfunding.entity.vo.DetailReturnVO;
import crowdfunding.entity.vo.PortalProjectVO;
import crowdfunding.entity.vo.PortalTypeVO;
import crowdfunding.mapper.MemberMapper;
import crowdfunding.mapper.ProjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MysqlMain.class)
public class MybatisTest {

    @Resource
    private DataSource dataSource;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private ProjectMapper projectMapper;

    private final Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }

    @Test
    public void testMemeberMapper() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String source = "123456";
        String encode = bCryptPasswordEncoder.encode(source);
        MemberPO memberPO = new MemberPO(null, "saber", encode, "呆毛王", "saber@saber.com", 2, 0, "阿尔托莉雅", "111111", 3);
        memberMapper.insert(memberPO);
    }

    @Test
    public void testProjectMapper() {
        List<PortalTypeVO> portalTypeVOList = projectMapper.selectPortalTypeVO();
        for (PortalTypeVO portalTypeVO : portalTypeVOList) {
            String name = portalTypeVO.getName();
            String remark = portalTypeVO.getRemark();
            logger.info("name = " + name + "remark = " + remark);
            List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
            for (PortalProjectVO portalProjectVO : portalProjectVOList) {
                if (portalProjectVO == null) {
                    continue;
                }
                logger.info(portalProjectVO.toString());
            }
        }
    }

    @Test
    public void testDetailProjectMapper() {
        DetailProjectVO detailProjectVO = projectMapper.selectDetailProjectVO(5);
        logger.info(detailProjectVO.getProjectId() + "");
        logger.info(detailProjectVO.getProjectName());
        logger.info(detailProjectVO.getProjectDescription());
        logger.info(detailProjectVO.getStatus() + "");
        logger.info(detailProjectVO.getMoney() + "");
        logger.info(detailProjectVO.getSupportMoney() + "");
        logger.info(detailProjectVO.getFollower() + "");
        logger.info(detailProjectVO.getCompletion() + "");
        logger.info(detailProjectVO.getSupporter() + "");
        logger.info(detailProjectVO.getDeployDate());
        logger.info(detailProjectVO.getHeaderPicturePath());
        List<String> detailPicturePathList = detailProjectVO.getDetailPicturePathList();
        for (String s : detailPicturePathList) {
            logger.info("pic_path" + s);
        }
        List<DetailReturnVO> detailReturnVOList = detailProjectVO.getDetailReturnVOList();
        for (DetailReturnVO detailReturnVO : detailReturnVOList) {
            logger.info(detailReturnVO.getReturnId() + "");
            logger.info(detailReturnVO.getContent());
            logger.info(detailReturnVO.getCount() + "");
            logger.info(detailReturnVO.getFreight() + "");
            logger.info(detailReturnVO.getReturnDate() + "");
            logger.info(detailReturnVO.getSupportMoney() + "");
        }
    }

}

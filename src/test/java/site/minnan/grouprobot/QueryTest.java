package site.minnan.grouprobot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.minnan.grouprobot.application.service.QueryService;
import site.minnan.grouprobot.userinterface.dto.Message;

@SpringBootTest(classes = GroupRobotApplication.class)
public class QueryTest {

    @Autowired
    private QueryService queryService;

    @Test
    public void testQueryByName(){
        Message message = new Message();
        message.setGroup_id(123L);
        message.setRaw_message("查询MinnanLum");
        queryService.queryByName(message);
    }
}

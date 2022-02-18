package site.minnan.grouprobot.application.service;

import org.springframework.http.ResponseEntity;
import site.minnan.grouprobot.userinterface.dto.Message;

/**
 * 查询服务
 *
 * @author Minnan on 2022/02/17
 */
public interface QueryService {

    /**
     * 按角色名称查询
     *
     * @param message 消息
     */
    void queryByName(Message message);

}

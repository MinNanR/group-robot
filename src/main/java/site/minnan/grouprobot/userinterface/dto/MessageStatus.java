package site.minnan.grouprobot.userinterface.dto;

import lombok.Data;

/**
 * 消息状态
 *
 * @author Minnan on 2022/02/17
 */
@Data
public class MessageStatus {

    private Boolean good;

    private Boolean app_good;

    private Boolean app_initialized;

    private Boolean online;

    private Boolean app_enabled;

    private Stat stat;
}

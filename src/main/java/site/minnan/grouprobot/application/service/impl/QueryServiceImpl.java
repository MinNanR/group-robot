package site.minnan.grouprobot.application.service.impl;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.minnan.grouprobot.application.service.QueryService;
import site.minnan.grouprobot.infrastrucutre.util.MessageUtil;
import site.minnan.grouprobot.userinterface.dto.Message;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Slf4j
@Service
public class QueryServiceImpl implements QueryService {

    private static final String QUERY_BY_NAME_BASE_URL = "https://api.maplestory.gg/v2/public/character/gms/";

    private static final char LINE_SEPARATOR = '\n';

    private static final Function<String, String> bracketText = s -> "(" + s + ")";

    private static final Function<Integer, String> rankBracket = rank -> "（排名" + rank + ")";

    private static final int million = 1000000;

    @Autowired
    private MessageUtil messageUtil;

    public static void main(String[] args) {
        byte[] bytes = HttpUtil.get(QUERY_BY_NAME_BASE_URL + "@").getBytes(StandardCharsets.UTF_8);
        System.out.println(new String(bytes));
    }

    /**
     * 按角色名称查询
     *
     * @param message
     */
    @Override
    public void queryByName(Message message) {
        String rawMessage = message.getRaw_message();
        String name = StrUtil.removePrefix(rawMessage, "查询");
        String queryUrl = QUERY_BY_NAME_BASE_URL + name.trim();
        byte[] responseBytes = HttpUtil.get(queryUrl).getBytes(StandardCharsets.UTF_8);
        if (responseBytes.length == 0) {
            log.warn("查询失败");
            return;
        }

        JSONObject responseJson = JSONUtil.parseObj(new String(responseBytes));
        JSONObject characterData = responseJson.getJSONObject("CharacterData");
        if (characterData == null) {
            log.warn("查询失败");
            return;
        }
        StringBuilder response = new StringBuilder();
        response.append("角色:").append(characterData.getStr("Name")).append(bracketText.apply(characterData.getStr(
                "Server")))
                .append(LINE_SEPARATOR)

                .append("等级:").append(characterData.getInt("Level")).append(" - ").append(characterData.getStr(
                "EXPPercent"))
                .append("%").append(rankBracket.apply(characterData.getInt("ServerRank"))).append(LINE_SEPARATOR)

                .append("职业:").append(characterData.getStr("Class")).append(rankBracket.apply(characterData.getInt(
                        "ServerClassRanking")))
                .append(LINE_SEPARATOR)

                .append("---------------------------------").append(LINE_SEPARATOR);

        if (characterData.getInt("AchievementPoints") != 0) {

            response.append("成就值:").append(characterData.getInt("AchievementPoints")).append(rankBracket.apply(characterData.getInt("AchievementRank")))
                    .append(LINE_SEPARATOR)

                    .append("联盟等级:").append(characterData.getInt("LegionLevel")).append(rankBracket.apply(characterData.getInt("LegionRank")))
                    .append(LINE_SEPARATOR)

                    .append("联盟战斗力:").append(characterData.getInt("LegionPower") / million).append("m")
                    .append(bracketText.apply("每日" + characterData.getInt("LegionCoinsPerDay") + "币"))
                    .append(LINE_SEPARATOR);
        }


        log.info(response.toString());


//        if (bytes.length == 0) {
//            messageUtil.sendGroupText(message.getGroup_id(), "查询失败");
//        } else {
//            String response = new String(bytes);
//            log.info(response);
//            messageUtil.sendGroupText(message.getGroup_id(), response);
//        }
    }

}

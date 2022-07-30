package top.strelitzia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.angelinaBot.annotation.AngelinaGroup;
import top.angelinaBot.model.MessageInfo;
import top.angelinaBot.model.ReplayInfo;
import top.strelitzia.dao.AdminUserMapper;

import java.io.File;


@Service
public class menuService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @AngelinaGroup(keyWords = {"详细菜单"}, description = "详细菜单")
    public ReplayInfo menu(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        replayInfo.setReplayImg(new File("runFile/menu/menu.jpg"));
        return replayInfo;
    }

    //@AngelinaGroup(keyWords = {"我有一个朋友"}, description = "我有一个朋友")
    public ReplayInfo friend(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);


        return replayInfo;
    }


}
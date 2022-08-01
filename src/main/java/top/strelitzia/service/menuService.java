package top.strelitzia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.angelinaBot.annotation.AngelinaGroup;
import top.angelinaBot.model.MessageInfo;
import top.angelinaBot.model.ReplayInfo;
import top.strelitzia.dao.AdminUserMapper;
import top.strelitzia.dao.UserFoundMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class menuService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private UserFoundMapper userFoundMapper;

    @AngelinaGroup(keyWords = {"详细菜单"}, description = "详细菜单")
    public ReplayInfo menu(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        replayInfo.setReplayImg(new File("runFile/menu/menu.jpg"));
        return replayInfo;
    }

    //@AngelinaGroup(keyWords = {"我有一个朋友"}, description = "我有一个朋友")
    public ReplayInfo friend(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        if(messageInfo.getArgs().size() == 3){
            String name = messageInfo.getArgs().get(1);
            name = name.substring(10,name.length()-1);
            long qq = Long.parseLong(name);
            String sent = messageInfo.getArgs().get(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            //获取当前时间String类型
            String time = sdf.format(date);
            //UserFoundInfo userFoundInfo = userFoundMapper.selectUserInfoByName(name);
            //根据昵称获取qq
            BufferedImage userImage = null;
            Font font = new Font("楷体", Font.BOLD, 50);
            //long qq = userFoundInfo.getQq();
            try {
                userImage = ImageIO.read(new URL("http://q.qlogo.cn/headimg_dl?dst_uin=" + qq + "&spec=100"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedImage image = new BufferedImage(670, 110, BufferedImage.TYPE_INT_BGR);
            Graphics g = image.getGraphics();
            g.setColor(Color.WHITE); // 先用白色填充整张图片,也就是背景
            g.drawImage(userImage, 10, 10, 100, 100, null);
            g.setFont(font);// 设置画笔字体
            g.drawString(name, 120, 60);
            g.drawString(time, 510, 60);
            g.drawString(sent, 120, 100);
            replayInfo.setReplayImg(image);
            replayInfo.setReplayMessage("测试消息：返回的qq为：" + qq );
        }else{
            replayInfo.setReplayMessage("请输入完整指令");
        }


        return replayInfo;
    }


}
package top.strelitzia.service;

import net.mamoe.mirai.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.angelinaBot.annotation.AngelinaGroup;
import top.angelinaBot.model.MessageInfo;
import top.angelinaBot.model.ReplayInfo;
import top.strelitzia.dao.RouletteMapper;
import top.strelitzia.model.RouletteInfo;

@Service
public class RouletteService {

    @Autowired
    private RouletteMapper rouletteMapper;

    @AngelinaGroup(keyWords = {"给轮盘上子弹","上膛","拔枪吧"}, description = "守护铳轮盘赌，看看谁是天命之子")
    public ReplayInfo Roulette(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        this.rouletteMapper.cleanRoulette();
        double r = Math.random();
        double bullet = 6 * r;
        this.rouletteMapper.rouletteTarget(messageInfo.getGroupId(), bullet);
        replayInfo.setReplayMessage("这是一把充满荣耀与死亡的守护铳，六个弹槽只有一颗子弹，不幸者将再也发不出声音。勇士们啊，扣动你们的扳机！感谢Outcast提供的守护铳！");
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"开枪"}, description = "进入生死的轮回")
    public ReplayInfo PetPet(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        RouletteInfo rouletteInfo = this.rouletteMapper.rouletteByQQ(messageInfo.getGroupId());
        if (rouletteInfo == null) {
            rouletteInfo = new RouletteInfo();
            rouletteInfo.setTrigger(Math.toIntExact(messageInfo.getGroupId()));
            rouletteInfo.setBullet(0.0);
        }
        double bullet = rouletteInfo.getBullet();
        int trigger = rouletteInfo.getTrigger();
        if (bullet == 6){
            replayInfo.setReplayMessage("您还没上子弹呢");
        }else {
            if(bullet > trigger){
                replayInfo.setMuted(60);
                //replayInfo.setMuted((new Random().nextInt(5) + 1) * 60);
                replayInfo.setReplayMessage("对不起，我也不想这样的......");
                this.rouletteMapper.cleanRoulette();
            }else {
                switch (trigger){
                    case 5:
                        replayInfo.setReplayMessage("无需退路。( 1 / 6 )");
                        break;

                    case 4:
                        replayInfo.setReplayMessage("英雄们，为这最强大的信念，请站在我们这边。( 2 / 6 )");
                        break;

                    case 3:
                        replayInfo.setReplayMessage("颤抖吧，在真正的勇敢面前。( 3 / 6 )");
                        break;

                    case 2:
                        replayInfo.setReplayMessage("哭嚎吧，为你们不堪一击的信念。( 4 / 6 ) ");
                        break;

                    case 1:
                        replayInfo.setReplayMessage("现在可没有后悔的余地了。( 5 / 6 )");
                        break;

                    case 0:
                        replayInfo.setReplayMessage("我的手中的这把Outcastd的守护铳，找了无数工匠都难以修缮如新。不......不该如此......");
                        this.rouletteMapper.cleanRoulette();
                        break;
                }
                trigger = trigger - 1;
                this.rouletteMapper.rouletteShoot(messageInfo.getGroupId(),trigger);
            }
        }
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"轮盘赌对决参赛"}, description = "六人参赛，一人丧命")
    public ReplayInfo RouletteDuel(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        RouletteInfo rouletteInfo = this.rouletteMapper.rouletteDuelByGroup(messageInfo.getGroupId());
        int Num;
        //意外情况清零
        try {
            Num = rouletteInfo.getParticipantNum();
        }catch (NullPointerException e)
        {
            this.rouletteMapper.cleanRouletteDuel();
            rouletteInfo = this.rouletteMapper.rouletteDuelByGroup(messageInfo.getGroupId());
            Num = rouletteInfo.getParticipantNum();
        }

        if( Num==0||Num>7 ){
            this.rouletteMapper.cleanRouletteDuel();
            Num = rouletteInfo.getParticipantNum();
        }

        switch (Num){
            case 1:
                replayInfo.setReplayMessage("这是一把充满荣耀与死亡的守护铳，六个弹槽只有一颗子弹，六位参赛者也将会有一位不幸者将再也发不出声音。\n欢迎第一位挑战者"+ messageInfo.getName() +"\n愿主保佑你，我的勇士。");
                this.rouletteMapper.rouletteParticipant1(messageInfo.getGroupId(),messageInfo.getQq());
                break;
            case 2:
                replayInfo.setReplayMessage("欢迎第二位挑战者"+messageInfo.getName()+"\n愿主保佑你，我的勇士。");
                this.rouletteMapper.rouletteParticipant2(messageInfo.getGroupId(),messageInfo.getQq());
                break;
            case 3:
                replayInfo.setReplayMessage("欢迎第三位挑战者"+messageInfo.getName()+"\n愿主保佑你，我的勇士。");
                this.rouletteMapper.rouletteParticipant3(messageInfo.getGroupId(),messageInfo.getQq());
                break;
            case 4:
                replayInfo.setReplayMessage("欢迎第四位挑战者"+messageInfo.getName()+"\n愿主保佑你，我的勇士。");
                this.rouletteMapper.rouletteParticipant4(messageInfo.getGroupId(),messageInfo.getQq());
                break;
            case 5:
                replayInfo.setReplayMessage("欢迎第五位挑战者"+messageInfo.getName()+"\n愿主保佑你，我的勇士。");
                this.rouletteMapper.rouletteParticipant5(messageInfo.getGroupId(),messageInfo.getQq());
                break;
            case 6:
                replayInfo.setReplayMessage("欢迎第六位挑战者"+messageInfo.getName()+"\n愿主保佑你，我的勇士。");
                this.rouletteMapper.rouletteParticipant6(messageInfo.getGroupId(),messageInfo.getQq());
                break;
            default:
                replayInfo.setReplayMessage(messageInfo.getName()+"，参赛人数已满，请等待下一场参赛吧");
                break;
        }
        return replayInfo;
    }

    @AngelinaGroup(keyWords = {"对决开始"}, description = "轮盘对决的生死抉择开始了")
    public ReplayInfo RouletteDuelBegging(MessageInfo messageInfo) {
        ReplayInfo replayInfo = new ReplayInfo(messageInfo);
        RouletteInfo rouletteInfo = this.rouletteMapper.rouletteDuelByGroup(messageInfo.getGroupId());
        Integer Num = rouletteInfo.getParticipantNum();
        //查询次数决定能不能开始
        if(Num < 6){
            replayInfo.setReplayMessage("参赛人数还不足六人，还不能开始对决呢。");
        }else{
            replayInfo.setMuted(60);
            this.rouletteMapper.cleanRoulette();
            long Qq = 0;
            double r = Math.random();
            double bullet = 6 * r;
            int finallyBullet = (int) Math.round(bullet);
            switch (finallyBullet){
                case 0:
                    replayInfo.setQq(rouletteInfo.getParticipantQQ1());
                    Qq = rouletteInfo.getParticipantQQ1();
                    break;
                case 1:
                    replayInfo.setQq(rouletteInfo.getParticipantQQ2());
                    Qq = rouletteInfo.getParticipantQQ2();
                    break;
                case 2:
                    replayInfo.setQq(rouletteInfo.getParticipantQQ3());
                    Qq = rouletteInfo.getParticipantQQ3();
                    break;
                case 3:
                    replayInfo.setQq(rouletteInfo.getParticipantQQ4());
                    Qq = rouletteInfo.getParticipantQQ4();
                    break;
                case 4:
                    replayInfo.setQq(rouletteInfo.getParticipantQQ5());
                    Qq = rouletteInfo.getParticipantQQ5();
                    break;
                default:
                    replayInfo.setQq(rouletteInfo.getParticipantQQ6());
                    Qq = rouletteInfo.getParticipantQQ6();
                    break;
            }
            //把获取到的禁言QQ带入禁言功能并且实现禁言

            String Name = Bot.getInstance(messageInfo.getLoginQq()).getGroup(messageInfo.getGroupId()).get(Qq).getNameCard();
            replayInfo.setMuted(5 * 60);
            replayInfo.setReplayMessage(Name + "，永别了，安息吧勇士......");
            this.rouletteMapper.cleanRouletteDuel();
        }
        return replayInfo;
    }

}

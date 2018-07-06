package com.weixin.mapper;

import com.weixin.model.weixin.WeixinUser;
import com.weixin.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface WeixinUserMapper extends MyMapper<WeixinUser> {
    /**
     * 关注记录
     * @param wu
     * @return
     */
    public void subscribe(WeixinUser wu);

    /**
     * 查询用户是否存在
     * @param openId
     * @return
     */
    public int isHaveDate(@Param("openId") String openId,@Param("weixinId") String weixinId);

    /**
     * 更新信息
     * @return
     */
    public void updateSubscribe(WeixinUser wu);
}

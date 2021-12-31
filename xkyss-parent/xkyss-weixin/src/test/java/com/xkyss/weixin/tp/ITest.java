package com.xkyss.weixin.tp;

import com.xkyss.core.util.ListUtil;
import com.xkyss.weixin.common.property.WxErrorCodeProperties;
import com.xkyss.weixin.tp.property.TpAppProperties;
import com.xkyss.weixin.tp.property.TpContactProperties;
import com.xkyss.weixin.tp.property.TpProperties;

public interface ITest {
    TpProperties CONFIG = TpProperties.builder()
            .serverUrl("http://192.168.0.71")
            .corpId("wwa7aee2f33ea96a11")
            .contact(TpContactProperties.builder()
                    .token("VR9xUHi7e8nVhdX6nLad")
                    .encodingAesKey("qMlJxEK0Fek7gPLK5xlL5oWjMdf0BtzN5zsZukeJw1w")
                    .secret("CPobCtmUoCHaZVcMxZXWxflb2gRPUNtKV4spYWdWnwo")
                    .build())
            .mainApp(TpAppProperties.builder()
                    .agentId("1000012")
                    .secret("1px2w5MGRzQ1cjJ8FvRVbrMmhpel4LxdQfXArf0KLXQ")
                    .build()
            )
            .build();

    WxErrorCodeProperties ERROR_CODES = WxErrorCodeProperties.builder()
            .refreshToken(ListUtil.of(42001L))
//            .refreshToken(ListUtil.of(42001L, 1201L))
            .build();
}

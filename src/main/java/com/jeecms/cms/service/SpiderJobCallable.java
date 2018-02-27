package com.jeecms.cms.service;

import java.util.List;

/**
 * Created by linzuk on 2018/2/26.
 */
public interface SpiderJobCallable {

    boolean callback(String id, String title, List<String> pictures);

}

package com.jeecms.cms.service;

import com.alibaba.fastjson.JSON;
import com.jeecms.cms.api.admin.main.ContentApiAct;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentExt;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.upload.FileUpload;
import com.jeecms.common.upload.FileUtil;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserSite;
import com.jeecms.core.manager.CmsUserMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by linzuk on 2018/2/26.
 */
@Service
public class SpiderJobCallableImpl implements SpiderJobCallable {

    @Autowired
    private ContentMng contentMng;
    @Autowired
    private CmsUserMng cmsUserMng;
//    private ContentApiAct contentApiAct;
    @Override
    public boolean callback(String id, String title, List<String> pictures) {
        // 打印数据
//        Map<String, Object> data = new HashMap<>();
//        data.put("id", id);
//        data.put("title", title);
//        data.put("pictures", pictures);
//        System.out.println(JSON.toJSONString(data));

        ContentExt ext = new ContentExt();
        ext.setTitle(id);
        ext.setTypeImg(pictures.get(0));
        CmsUser user = cmsUserMng.findByUsername("admin");
        String[] array = new String[pictures.size()];
        String[] pics=pictures.toArray(array);
        Content bean = new Content();
        CmsSite site = new CmsSite();
        site.setDomain("localhsot");
        site.setPath("www");
        site.setName("jeecms演示站");
        site.setShortName("jeecms演示站");
        site.setProtocol("http://");
        site.setDynamicSuffix(".jhtml");
        site.setStaticSuffix(".html");
        site.setStaticDir("/html");
        site.setStaticMobileDir("/wap");
        site.setIndexToRoot(false);
        site.setStaticIndex(false);
        site.setLocaleAdmin("zh_CN");
        site.setLocaleFront("zh_CN");
        site.setTplSolution("default");
        site.setTplMobileSolution("mobile");
        site.setFinalStep((byte) 3);
        site.setAfterCheck((byte) 3);
        site.setRelativePath(false);
        site.setResycleOn(true);
        site.setKeywords("JEECMS－JAVA网站内容管理系统|开源java cms系统,jsp cms网站管理");
        site.setDescription("JEECMS-国内优秀的JAVA(JSP)网站内容管理系统");
        site.setMobileStaticSync(false);
        site.setPageSync(false);
        site.setResouceSync(false);
        Set<CmsUserSite> userSet = new HashSet<>();
        CmsUserSite cmsUserSite = new CmsUserSite();
        cmsUserSite.setUser(user);
        cmsUserSite.setId(2);
        cmsUserSite.setSite(site);
        site.setUserSites(userSet);


        bean.setSite(site);
//        String[] titles = title.split("//");
        for(int i=0;i<pictures.size();i++){
            try {
                FileUtil.uploadImgLW(pictures.get(i).toString(),title,String.valueOf(i));
                contentMng.savePY(bean,ext,null,null,null,null,null,null,null,null,pics,null,76,2,false,null,null,null,false,0.0,10.0,null,user,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        contentMng.savePY(bean,ext,null,null,null,null,null,null,null,null,pics,null,76,2,false,null,null,null,false,0.0,10.0,null,user,true);
//        contentApiAct.save(null,ext,null,null,"","","","","",pictures.toArray().toString(),"",2,null,"",false,null,5,null,null,false,0.0,10.0,"", null, null);
        // TODO: 将数据录入系统就好
        return false; // true: 之前已经录入过了
    }
}
// contentApiAct.save("","","","");
//        save(Content bean,ContentExt ext,ContentTxt txt,
//                Boolean copyimg,String channelIds,String topicIds,String viewGroupIds,
//                String attachmentPaths,String attachmentNames,
//                String picPaths,String picDescs,Integer channelId, Integer typeId,
//                String tagStr, Boolean draft,Integer cid, Integer modelId,Short charge,
//                Double chargeAmount,Boolean rewardPattern,Double rewardRandomMin,Double rewardRandomMax,
//                String rewardFix,HttpServletRequest request,HttpServletResponse response)
//        Content save(Content bean, ContentExt ext, ContentTxt txt,Integer[] channelIds,
//                Integer[] topicIds, Integer[] viewGroupIds, String[] tagArr,
//                String[] attachmentPaths, String[] attachmentNames,
//                String[] attachmentFilenames, String[] picPaths,
//                String[] picDescs, Integer channelId, Integer typeId,
//                Boolean draft, Boolean contribute,Short charge,
//                Double chargeAmount,Boolean rewardPattern,
//                Double rewardRandomMin,Double rewardRandomMax,
//                Double[] rewardFix,CmsUser user,
//        boolean forMember)

package com.inext.service.impl;

import com.inext.dao.IPermissionInfoDao;
import com.inext.entity.PermissionInfo;
import com.inext.entity.SysModule;
import com.inext.service.IPermissionInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：jzhang
 * 创建时间：2017-04-28 下午 13:46
 */
@Service
public class PermissionInfoService implements IPermissionInfoService {
    @Resource
    IPermissionInfoDao iPermissionInfoDao;

    @Override
    public int insert(PermissionInfo black) {
        return iPermissionInfoDao.insert(black);
    }

    @Override
    public List<PermissionInfo> getInfoByUserId(int userId) {
        PermissionInfo info = new PermissionInfo();
        info.setUserId(userId);
        return iPermissionInfoDao.getInfoByUserId(userId);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        PermissionInfo info = new PermissionInfo();
        info.setUserId(userId);
        return iPermissionInfoDao.delete(info);
    }

    @Override
    public String getModuleIdsByUserId(Integer userId) {
        // TODO Auto-generated method stub
        return iPermissionInfoDao.getModuleIdsByUserId(userId);
    }

    @Override
    public int deleteByModuleId(Integer moduleId) {
        PermissionInfo info = new PermissionInfo();
        info.setModuleId(moduleId);
        return iPermissionInfoDao.delete(info);
    }

    @Override
    public String getUserModule(Integer backUserId) {
        // TODO Auto-generated method stub
        Map<String, Object> params = new HashMap<>();
        params.put("backUserId", backUserId);
        params.put("status", SysModule.DISPLAT_STATUS_SHOW + "");
        params.put("enabled", SysModule.ENABLE_STATUS_USABLE + "");
        params.put("parentId", SysModule.SUPER + "");
        List<SysModule> sysModuleList = iPermissionInfoDao.getModuleByUserId(params);
        String html = appendChildren(sysModuleList, params).toString();
        return html;
    }

    private StringBuffer appendChildren(List<SysModule> sysModuleList, Map<String, Object> params) {
        StringBuffer html = new StringBuffer();
//      for (SysModule sysModule : sysModuleList) {
//          html.append("<li class=\"xn-openable\">");
//          html.append(" <a href=\"#\"><span class=\"fa "+sysModule.getIconClass()+"\"></span> <span class=\"xn-text\">"+sysModule.getName() +"</span></a>");
//          params.put("parentId", sysModule.getId()+"");
//          List<SysModule> children = this.iPermissionInfoDao.getModuleByUserId(params);
//          if (children != null && children.size() != 0) {
//              html.append("<ul>");
//              for (SysModule childrenModule : children) {
//            	  html.append("<li class=\"xn-openable\">");
//                  html.append("<a href=\"" + childrenModule.getUrl() + "\" target='external-frame'\n\" ><span class=\"fa "+childrenModule.getIconClass()+"\"></span> " + childrenModule.getName() + "</a>");
//                  html.append("</li>");
//              }
//              html.append("</ul>");
//          } else {
//
//          }
//          html.append("</li>");
//      }
        for (SysModule sysModule : sysModuleList) {
            params.put("parentId", sysModule.getId() + "");
            List<SysModule> children = this.iPermissionInfoDao.getModuleByUserId(params);
            if (children != null && children.size() != 0) {
                html.append("<li class=\"xn-openable\">");
                html.append(" <a href=\"#\"><span class=\"fa " + sysModule.getIconClass() + "\"></span> <span class=\"xn-text\">" + sysModule.getName() + "</span></a>");
                html.append("<ul>");
                html.append(appendChildren(children, params).toString());
                html.append("</ul>");
                html.append("</li>");
            } else {
                html.append("<li>");
                html.append("<a href=\"" + sysModule.getUrl() + "\" target='external-frame'\n\" ><span class=\"fa " + sysModule.getIconClass() + "\"></span> " + sysModule.getName() + "</a>");
                html.append("</li>");
            }
        }
        return html;
    }

    @Override
    public int deleteByModuleIdsAndUserIds(String delUserIds, String delModuleIds) {
        // TODO Auto-generated method stub
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("delUserIds", delUserIds);
        params.put("delModuleIds", delModuleIds);
        return iPermissionInfoDao.deleteByModuleIdsAndUserIds(params);
    }
}

package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.constants.ErrorCodeMsgConstant;
import com.inext.constants.RedisCacheConstants;
import com.inext.dao.IBackUserDao;
import com.inext.entity.BackUser;
import com.inext.entity.PermissionInfo;
import com.inext.entity.SysModule;
import com.inext.entity.SysUserRole;
import com.inext.service.IBackUserService;
import com.inext.service.IPermissionInfoService;
import com.inext.service.ISysUserRoleService;
import com.inext.utils.MD5coding;
import com.inext.utils.RedisUtil;
import com.inext.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("backUserService")
public class BackUserService implements IBackUserService {

    private static Logger logger = LoggerFactory.getLogger(BackUserService.class);
    @Resource
    ISysUserRoleService sysUserRoleService;
    @Resource
    IPermissionInfoService iPermissionInfoService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    @Qualifier("backUserDao")
    private IBackUserDao backUserDao;

    public BackUser findBackUserById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", id.trim());
        BackUser backUser = backUserDao.findByAccount(params);
        return backUser;
    }

    @Override
    public Map<String, Object> saveBackUser(BackUser backUser) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer id = null;
        if (!backUser.getOldPassword().equals(backUser.getPassword())) {
            String Md5 = MD5coding.getInstance().code(backUser.getPassword());//加密
            backUser.setPassword(Md5);
        }
        if (backUser.getId() == null) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userAccount", backUser.getAccount());
            if (backUserDao.findByAccount(params) != null) {
                resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_USER_NO_FIND);
                resultMap.put("errorMsg", "已存在同名的的管理用户,请重新输入");
                return resultMap;
            }
            if (StringUtils.isNotBlank(backUser.getNickName())) {
                params.put("nickName", backUser.getNickName());
                if (backUserDao.findByNickName(params) != null) {
                    resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_USER_NO_FIND);
                    resultMap.put("errorMsg", "昵称已经被别人占用,请重新输入");
                    return resultMap;
                }
            }
            backUser.setCreateTime(new Date());
            backUser.setCreateAccount(backUser.getUpdateAccount());
            backUserDao.insertSelective(backUser);
            id = backUserDao.findByAccount(params).getId();

        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("id", backUser.getId());
            params.put("userAccount", backUser.getAccount());
            if (backUserDao.findByAccountNotId(params) != null) {
                resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_USER_NO_FIND);
                resultMap.put("errorMsg", "已存在同名的的管理用户,请重新输入");
                return resultMap;
            }
            if (StringUtils.isNotBlank(backUser.getNickName())) {
                params.put("nickName", backUser.getNickName());
                if (backUserDao.findByNickNameNotId(params) != null) {
                    resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_USER_NO_FIND);
                    resultMap.put("errorMsg", "昵称已经被别人占用,请重新输入");
                    return resultMap;
                }
            }
            if (backUser.getDiversionChannel() == null) {
                backUser.setDiversionChannel("");
            }
            if (backUser.getPublishingChannel() == null) {
                backUser.setPublishingChannel("");
            }
            backUserDao.updateByPrimaryKeySelective(backUser);
            id = backUser.getId();
            if (StringUtils.isNotBlank(backUser.getStatus())) {
                if ("0".equals(backUser.getStatus())) {
                    addBackUserBlacklist(backUser.getId().toString());
                } else if ("1".equals(backUser.getStatus())) {
                    delBackUserBlacklist(backUser.getId().toString());
                }
            }
        }
        saveUserRole(id, backUser.getUserRoles(), backUser.getUserModules());//保存用户角色 用户菜单
        resultMap.put("id", id);
        resultMap.put("errorMsg", "保存成功");
        return resultMap;
    }

    /**
     * 保存用户角色关系
     */
    private void saveUserRole(Integer userId, String roleId, String moduleId) {
        if (StringUtils.isNotBlank(roleId)) {
            String[] roleIds = roleId.split(",");
            if (roleIds != null && roleIds.length > 0) {
                this.sysUserRoleService.removeSysUserRoleByUserId(userId);
                for (String rid : roleIds) {
                    if (StringUtils.isNotBlank(rid)) {
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setRoleId(Integer.parseInt(rid));
                        sysUserRole.setUserId(userId);
                        this.sysUserRoleService.saveUserRole(sysUserRole);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(moduleId)) {
            String[] module = moduleId.split(",");
            if (module != null && module.length > 0) {
                iPermissionInfoService.deleteByUserId(userId);
                for (String tem : module) {
                    if (StringUtils.isNotBlank(tem) && !SysModule.SUPER.toString().equals(tem)) {
                        PermissionInfo info = new PermissionInfo();
                        info.setUserId(userId);
                        info.setModuleId(Integer.parseInt(tem));
                        info.setCreateTime(new Date());
                        iPermissionInfoService.insert(info);
                    }
                }
            }
        }
    }

    @Override
    public PageInfo<BackUser> getPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<BackUser> list = this.backUserDao.getPageList(params);
        PageInfo<BackUser> pageInfo = new PageInfo<BackUser>(list);
        return pageInfo;
    }

    @Override
    public int del(String id) {
        BackUser operbackUser = (BackUser) RequestUtils.getRequest().getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        BackUser backUser = new BackUser();
        backUser.setId(Integer.parseInt(id));
        backUser.setStatus("0");
        backUser.setUpdateAccount(operbackUser.getAccount());
        backUser.setUpdateTime(new Date());
//		addBackUserBlacklist(id);
        return backUserDao.updateByPrimaryKeySelective(backUser);
    }

    @Override
    public List<BackUser> getListByRoleId(Integer roleId) {
        return backUserDao.getListByRoleId(roleId);
    }

    @Override
    public Map<String, Object> updateUserInfo(BackUser backUser) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(backUser.getOldPassword())) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("id", backUser.getId());
            BackUser oldBackUser = backUserDao.findByAccount(params);
            if (!oldBackUser.getPassword().equals(MD5coding.getInstance().code(backUser.getOldPassword()))) {
                resultMap.put("errorCode", -1);
                resultMap.put("errorMsg", "旧密码输入错误");
                return resultMap;
            }
            String Md5 = MD5coding.getInstance().code(backUser.getPassword());//加密
            backUser.setPassword(Md5);
        } else {
            backUser.setPassword(null);
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", backUser.getId());
        if (StringUtils.isNotBlank(backUser.getNickName())) {
            params.put("nickName", backUser.getNickName());
            if (backUserDao.findByNickNameNotId(params) != null) {
                resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_USER_NO_FIND);
                resultMap.put("errorMsg", "昵称已经被别人占用,请重新输入");
                return resultMap;
            }
        }
        backUserDao.updateByPrimaryKeySelective(backUser);
        resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_SUCCESS);
        resultMap.put("errorMsg", "修改成功");
        return resultMap;
    }

    @Override
    public void addBackUserBlacklist(String userId) {
        // TODO Auto-generated method stub
        Map<String, Object> backUserBlacklist = (Map<String, Object>) redisUtil.get(BackUser.BackUserBlacklist);
        if (backUserBlacklist == null) {
            backUserBlacklist = new HashMap<>();
        }
        backUserBlacklist.put(userId, null);
        redisUtil.set(BackUser.BackUserBlacklist, backUserBlacklist);
    }

    @Override
    public void delBackUserBlacklist(String userId) {
        // TODO Auto-generated method stub
        Map<String, Object> backUserBlacklist = (Map<String, Object>) redisUtil.get(BackUser.BackUserBlacklist);
        if (backUserBlacklist == null) {
            backUserBlacklist = new HashMap<>();
        }
        backUserBlacklist.remove(userId);
        redisUtil.set(BackUser.BackUserBlacklist, backUserBlacklist);
    }

    @Override
    public Map<String, Object> checkLogin(HashMap<String, Object> params) {
        logger.info("BackLoginService check start ,param:" + params);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        BackUser backUser = backUserDao.findByAccount(params);
        if (null == backUser) {
            resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_USER_NO_FIND);
            resultMap.put("errorMsg", ErrorCodeMsgConstant.MSG_USER_NO_FIND);
            return resultMap;
        } else if (!"1".equals(backUser.getStatus())) {
            resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_USER_NO_FIND);
            resultMap.put("errorMsg", ErrorCodeMsgConstant.MSG_USER_NO_FIND);
            return resultMap;
        }
        if ("0".equals(String.valueOf(params.get("loginType")))) {
            if (!backUser.getPassword().equals(MD5coding.getInstance().code(String.valueOf(params.get("userPassword"))))) {
                resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_PASSWORD_ERROR);
                resultMap.put("errorMsg", ErrorCodeMsgConstant.MSG_PASSWORD_ERROR);
                return resultMap;
            }
        } else {
            String key = RedisCacheConstants.SMS_CODE_VERIFICATION_LOGIN_PREFIX + params.get("userAccount");
            if (params.containsKey("mobileCode") && !params.get("mobileCode").equals(redisUtil.get(key))) {
                resultMap.put("errorCode", ErrorCodeMsgConstant.CODE_MOBILECODE_ERROR);
                resultMap.put("errorMsg", ErrorCodeMsgConstant.MSG_MOBILECODE_ERROR);
                return resultMap;
            }
        }

        resultMap.put("backUser", backUser);
        return resultMap;

    }
}

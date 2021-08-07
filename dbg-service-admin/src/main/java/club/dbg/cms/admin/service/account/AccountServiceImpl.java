package club.dbg.cms.admin.service.account;

import club.dbg.cms.domain.admin.AccountDO;
import club.dbg.cms.domain.admin.RoleDO;
import club.dbg.cms.admin.dao.AccountMapper;
import club.dbg.cms.admin.dao.AccountRoleMapper;
import club.dbg.cms.admin.dao.RoleMapper;
import club.dbg.cms.admin.dao.RolePermissionMapper;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.account.pojo.AccountDTO;
import club.dbg.cms.admin.service.account.pojo.AccountListDTO;
import club.dbg.cms.admin.service.role.RoleService;
import club.dbg.cms.admin.service.role.pojo.AccountRoleDetail;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.util.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashSet;
import java.util.List;

/**
 * @author dbg
 */
@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountMapper accountMapper;

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    private final AccountRoleMapper accountRoleMapper;

    private final RolePermissionMapper rolePermissionMapper;

    private final DataSourceTransactionManager transactionManager;

    public AccountServiceImpl(AccountMapper accountMapper,
                              RoleService roleService,
                              AccountRoleMapper accountRoleMapper,
                              RoleMapper roleMapper,
                              RolePermissionMapper rolePermissionMapper,
                              DataSourceTransactionManager transactionManager) {
        this.accountMapper = accountMapper;
        this.roleService = roleService;
        this.accountRoleMapper = accountRoleMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.transactionManager = transactionManager;
    }

    @Override
    public AccountListDTO getAccountList(String username, int page, int pageSize) {
        page = (page - 1) * pageSize;
        AccountListDTO accountListDTO = new AccountListDTO();
        accountListDTO.setAccountList(accountMapper.selectAccountList(username, page, pageSize));
        accountListDTO.setTotal(accountMapper.countAccountList(username));
        return accountListDTO;
    }

    @Override
    public AccountDTO getAccountDetail(Integer accountId) {
        AccountDTO accountDTO = new AccountDTO();
        AccountDO accountDO = accountMapper.selectAccountById(accountId);
        if (accountDO == null) {
            throw new BusinessException("用户不存在");
        }
        accountDTO.setId(accountDO.getId());
        accountDTO.setUsername(accountDO.getUsername());
        accountDTO.setRealName(accountDO.getRealName());
        accountDTO.setEmail(accountDO.getEmail());
        accountDTO.setSex(accountDO.getSex());
        accountDTO.setLastTime(accountDO.getLastTime());
        accountDTO.setLastIp(accountDO.getLastIp());
        //获取所拥有的角色
        HashSet<Integer> roleIds = accountRoleMapper.selectRoleIdByAccountId(accountId);
        List<RoleDO> roleList = roleMapper.selectRoleByIds(roleIds);
        HashSet<Integer> roleLevels = new HashSet<>();
        HashSet<String> roles = new HashSet<>();
        for (RoleDO roleDO : roleList) {
            roleLevels.add(roleDO.getRoleLevel());
            roles.add(roleDO.getRoleName());
        }
        accountDTO.setRoleIds(roleIds);
        accountDTO.setRoles(roles);
        accountDTO.setRoleLevels(roleLevels);
        // 获取所拥有的权限
        //HashSet<Integer> permissionList = rolePermissionMapper.selectPermissionIdByRoleIds(roleIds);
        //accountDTO.setPermissionSet(permissionList);
        return accountDTO;
    }

    @Override
    synchronized public Boolean addAccount(Operator operator, AccountDTO account) {
        AccountDO accountCheck = accountMapper.selectAccountByUsername(account.getUsername());
        if (accountCheck != null) {
            throw new BusinessException("用户已存在");
        }

        AccountDO accountDO = new AccountDO();
        accountDO.setUsername(account.getUsername());
        accountDO.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
        accountDO.setRealName(account.getRealName());
        accountDO.setCreateTime(System.currentTimeMillis() / 1000);
        accountDO.setUpdateTime(accountDO.getCreateTime());
        accountDO.setEmail(account.getEmail());
        accountDO.setRegisterTime(accountDO.getCreateTime());
        accountDO.setLastTime(accountDO.getCreateTime());
        accountDO.setLastIp("");
        accountDO.setSex(account.getSex());
        accountDO.setStatus(account.getStatus());

        TransactionStatus transactionStatus = this.getTransactionStatus();
        try {
            if (accountMapper.insertAccount(accountDO) != 1) {
                throw new BusinessException("添加用户失败");
            }
            account.setId(accountDO.getId());
            if (!roleService.setAccountRole(operator, account, account.getRoleIds())) {
                throw new BusinessException("赋予角色失败");
            }
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.warn("添加用户异常：", e);
            transactionManager.rollback(transactionStatus);
            throw e;
        }
        return true;
    }

    /**
     * TODO 账号信息和所拥有的角色信息放在同一次查询中
     */
    @Override
    synchronized public Boolean editAccount(Operator operator, AccountDTO account) {
        AccountDO accountCheck = accountMapper.selectAccountById(account.getId());
        HashSet<Integer> roleIds = accountRoleMapper.selectRoleIdByAccountId(account.getId());
        AccountDO accountDO = accountHandel(accountCheck, account);
        boolean isUpdate = false;
        TransactionStatus transactionStatus = getTransactionStatus();
        try {
            if (accountDO != null) {
                accountDO.setId(account.getId());
                if (accountMapper.updateAccount(accountDO) != 1) {
                    throw new BusinessException("修改用户信息失败");
                }
                isUpdate = true;
            }
            if (!roleEquals(roleIds, account.getRoleIds()) || roleIds.size() == 0) {
                if (!roleService.setAccountRole(operator, account, roleIds)) {
                    throw new BusinessException("修改用户信息失败");
                }
                isUpdate = true;
            }
            if (isUpdate) {
                transactionManager.commit(transactionStatus);
            }
        } catch (Exception e) {
            log.warn("修改用户信息异常：", e);
            transactionManager.rollback(transactionStatus);
            throw e;
        }
        if (!isUpdate) {
            throw new BusinessException("未作出任何修改");
        }
        return true;
    }

    private Boolean roleEquals(HashSet<Integer> roleIds, HashSet<Integer> checkRoleIds) {
        if (roleIds.size() != checkRoleIds.size()) {
            return false;
        }
        int count = 0;
        for (Integer roleId : roleIds) {
            for (Integer roleCheck : checkRoleIds) {
                if (roleId.equals(roleCheck)) {
                    count++;
                    break;
                }
            }
        }
        return roleIds.size() == count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAccount(Operator operator, Integer accountId) {
        AccountDO accountDO = accountMapper.selectAccountById(accountId);
        if (accountDO == null) {
            throw new BusinessException("用户不存在");
        }
        List<AccountRoleDetail> accountRoleList = accountRoleMapper.selectAccountRoleByAccountId(accountId);
        if (accountRoleList.isEmpty()) {
            if (accountMapper.deleteAccount(accountId) != 1) {
                throw new BusinessException("删除失败");
            }
            return true;
        }
        if (operator.getRoleLevel() >= accountRoleList.get(0).getRoleLevel()) {
            throw new BusinessException("权限不足，无法删除该用户");
        }
        if (accountMapper.deleteAccount(accountId) != 1) {
            throw new BusinessException("删除失败");
        }
        return true;
    }

    @Override
    public Boolean deleteAccounts(Operator operator, List<Integer> idList) {
        return false;
    }

    @Override
    public Boolean changePassword(int id, String passwordOld, String passwordNew) {
        AccountDO accountDO = accountMapper.selectAccountById(id);
        if (accountDO == null) {
            throw new BusinessException("修改密码失败");
        }
        if (!BCrypt.checkpw(passwordOld, accountDO.getPassword())) {
            return false;
        }
        accountDO.setPassword(BCrypt.hashpw(passwordNew, BCrypt.gensalt()));
        accountMapper.updateAccountPassword(accountDO);
        return true;
    }

    @Override
    public Boolean resetPassword(int id, String code, String passwordNew) {
        return false;
    }

    @Override
    public Boolean resetEmail(int id, String code, String emailNew) {
        return false;
    }

    private AccountDO accountHandel(AccountDO accountCheck, AccountDTO account) {
        boolean update = false;
        AccountDO accountDO = new AccountDO();
        if (accountCheck.getUsername().equals(account.getUsername())) {
            accountDO.setUsername(null);
        } else {
            accountDO.setUsername(account.getUsername());
            update = true;
        }
        if (accountCheck.getRealName().equals(account.getRealName())) {
            accountDO.setRealName(null);
        } else {
            accountDO.setRealName(account.getRealName());
            update = true;
        }
        if (accountCheck.getEmail().equals(account.getEmail())) {
            accountDO.setEmail(null);
        } else {
            accountDO.setEmail(account.getEmail());
            update = true;
        }
        if (accountCheck.getStatus().equals(account.getStatus())) {
            accountDO.setStatus(-1);
        } else {
            accountDO.setStatus(account.getStatus());
            update = true;
        }
        if (!accountCheck.getSex().equals(account.getSex())) {
            accountDO.setSex(account.getSex());
            update = true;
        } else {
            accountDO.setSex(account.getSex());
        }
        if (!update) {
            return null;
        }
        return accountDO;
    }

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(transDefinition);
    }
}

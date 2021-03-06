package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.config.ConfigConsts;
import club.dbg.cms.admin.filter.pojo.MyHttpServletRequest;
import club.dbg.cms.admin.service.account.AccountService;
import club.dbg.cms.admin.service.account.pojo.AccountDTO;
import club.dbg.cms.admin.service.account.pojo.AccountListDTO;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.util.ResponseBuild;
import com.alibaba.fastjson.JSON;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;

/**
 * @author dbg
 */
@Validated
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/get_account_list", method = RequestMethod.GET, name = "获取用户列表")
    public ResponseBuild<AccountListDTO> getAccountList(@RequestParam(value = "username", required = false) String username,
                                                        @RequestParam("page") Integer page,
                                                        @RequestParam("pageSize") Integer pageSize) {
        return ResponseBuild.ok(accountService.getAccountList(username, page, pageSize));
    }

    @RequestMapping(value = "/user_info", method = RequestMethod.GET, name = "获取用户信息")
    public ResponseBuild<AccountDTO> getAccountDetail(MyHttpServletRequest request) {
        Integer accountId = request.getOperator().getId();
        return ResponseBuild.ok(accountService.getAccountDetail(accountId));
    }

    @RequestMapping(value = "/add_account", method = RequestMethod.POST, name = "创建账号")
    public ResponseBuild<Boolean> addAccount(
            MyHttpServletRequest request,
            @Length(min = ConfigConsts.USERNAME_MIN,
                    max = ConfigConsts.USERNAME_MAX,
                    message = ConfigConsts.USERNAME_DESCRIBE)
            @RequestParam("username") String username,
            @Length(min = ConfigConsts.PASSWORD_MIN,
                    max = ConfigConsts.PASSWORD_MAX,
                    message = ConfigConsts.PASSWORD_DESCRIBE)
            @RequestParam("password") String password,
            @Length(min = 2,
                    max = 16,
                    message = "姓名长度在2-16")
            @RequestParam("realName") String realName,
            @Length(min = 4,
                    max = 60,
                    message = "邮箱长度在4-60")
            @RequestParam("email") String email,
            @Range(max = 1)
            @RequestParam("sex") Integer sex,
            @Range(max = 1)
            @RequestParam("status") Integer status,
            @RequestParam("roleIds") HashSet<Integer> roleIds) {
        Operator operator = request.getOperator();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername(username);
        accountDTO.setPassword(password);
        accountDTO.setRealName(realName);
        accountDTO.setEmail(email);
        accountDTO.setSex(sex);
        accountDTO.setStatus(status);
        accountDTO.setRoleIds(roleIds);
        return ResponseBuild.ok(accountService.addAccount(operator, accountDTO));
    }

    @RequestMapping(value = "/edit_account", method = RequestMethod.POST, name = "编辑账号")
    public ResponseBuild<Boolean> editAccount(
            MyHttpServletRequest request,
            @Range
            @RequestParam("id") Integer id,
            @Length(min = 2,
                    max = 16,
                    message = "姓名长度在2-16")
            @RequestParam("realName") String realName,
            @Length(min = 4,
                    max = 60,
                    message = "邮箱长度在4-60")
            @RequestParam("email") String email,
            @Range(max = 1)
            @RequestParam("sex") Integer sex,
            @Range(max = 1)
            @RequestParam("status") Integer status,
            @RequestParam("roleIds") HashSet<Integer> roleIds) {
        Operator operator = request.getOperator();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setRealName(realName);
        accountDTO.setEmail(email);
        accountDTO.setSex(sex);
        accountDTO.setStatus(status);
        accountDTO.setRoleIds(roleIds);
        return ResponseBuild.ok(accountService.editAccount(operator, accountDTO));
    }

    @RequestMapping(value = "/delete_account", method = RequestMethod.POST, name = "删除账号")
    public ResponseBuild<Boolean> deleteAccount(
            MyHttpServletRequest request,
            @NotNull(message = "id不能为空")
            @Min(value = 0, message = "id大于等于0")
            @RequestParam("id") Integer id) {
        Operator operator = request.getOperator();
        return ResponseBuild.ok(accountService.deleteAccount(operator, id));
    }

    @RequestMapping(value = "/delete_accounts", method = RequestMethod.POST, name = "删除账号（批量）")
    public ResponseBuild<String> deleteAccounts(@RequestParam("idList") List<Integer> idList) {
        return ResponseBuild.bad("暂不提供服务");
    }

    @RequestMapping(value = "/update_password", method = RequestMethod.POST, name = "修改密码")
    public ResponseBuild<Boolean> changePassword(
            MyHttpServletRequest request,
            @RequestParam("passwordOld") String passwordOld,
            @RequestParam("passwordNew") String passwordNew) {
        Integer id = request.getOperator().getId();
        return ResponseBuild.ok(accountService.changePassword(id, passwordOld, passwordNew));
    }
}

package club.dbg.cms.admin.config;

/**
 * 访问参数校验
 */
public class ConfigConsts {
    // id最小值
    public static final int ID_MIN = 0;

    // id最大值
    public static final int ID_MAX = 2147483647;

    // id描述
    public static final String ID_DESCRIBE = "id范围：" + ID_MIN + "-" + ID_MAX;

    // 页数最小值
    public static final int PAGE_MIN = 1;

    // 页数最大值
    public static final int PAGE_MAX = 10000000;

    // 页数范围描述
    public static final String PAGE_DESCRIBE = "页数范围：" + PAGE_MIN + "-" + PAGE_MAX;

    // 页大小最小值
    public static final int PAGE_SIZE_MIN = 1;

    // 页大小最大值
    public static final int PAGE_SIZE_MAX = 200;

    // 页大小范围描述
    public static final String PAGE_SIZE_DESCRIBE = "页大小：" + PAGE_SIZE_MIN + "-" + PAGE_SIZE_MAX;

    // 用户名最小长度
    public static final int USERNAME_MIN = 4;

    // 用户名最大长度
    public static final int USERNAME_MAX = 16;

    // 用户名描述
    public static final String USERNAME_DESCRIBE = "用户名长度：" + USERNAME_MIN + "-" + USERNAME_MAX;

    // 密码最小长度
    public static final int PASSWORD_MIN = 8;

    // 密码最大长度
    public static final int PASSWORD_MAX = 16;

    // 密码名描述
    public static final String PASSWORD_DESCRIBE = "密码长度：" + PASSWORD_MIN + "-" + PASSWORD_MAX;

    // UP名字长度最小值
    public static final int UP_MIN = 1;

    // UP名字长度最大值
    public static final int UP_MAX = 16;

    // UP描述
    public static final String UP_DESCRIBE = "up名字长度：" + UP_MIN + "-" + UP_MAX;

    // 房间号最小值
    public static final int ROOM_ID_MIN = 0;

    // 房间号最大值
    public static final int ROOM_ID_MAX = 999999999;

    // 房间号描述
    public static final String ROOM_ID_DESCRIBE = "房间号范围：" + ROOM_ID_MIN + "-" + ROOM_ID_MAX;

    // 查询时status最小值
    public static final int QUERY_STATUS_MIN = -1;

    // 查询时status最大值
    public static final int QUERY_STATUS_MAX = 1;

    public static final String QUERY_STATUS_DESCRIBE = "查询状态范围：" + QUERY_STATUS_MIN + "-" + QUERY_STATUS_MAX;

    // 数据变动操作时status的最小值
    public static final int DATA_STATUS_MIN = 0;

    // 数据变动操作时status的最大值
    public static final int DATA_STATUS_MAX = 1;

    public static final String DATA_STATUS_DESCRIBE = "status范围：" + DATA_STATUS_MIN + "-" + DATA_STATUS_MAX;

    // 数据变动操作时服务id最小值
    public static final int DATA_SERVICE_ID_MIN = 0;

    // 数据变动操作时服务id最大值
    public static final int DATA_SERVICE_ID_MAX = 1000;

    // 数据变动操作时服务id描述
    public static final String DATA_SERVICE_ID_DESCRIBE = "服务编号范围：" + DATA_SERVICE_ID_MIN + "-" + DATA_SERVICE_ID_MAX;

    // 查询时服务id最小值
    public static final int QUERY_SERVICE_ID_MIN = -1;

    // 查询时服务id最大值
    public static final int QUERY_SERVICE_ID_MAX = 1000;

    // 查询时服务id描述
    public static final String QUERY_SERVICE_ID_DESCRIBE = "查询时服务id范围：" + DATA_SERVICE_ID_MIN + "-" + DATA_SERVICE_ID_MAX;

    // 服务名最小长度
    public static final int SERVICE_NAME_MIN = 6;

    // 服务名最大长度
    public static final int SERVICE_NAME_MAX = 30;

    // 服务名描述
    public static final String SERVICE_NAME_DESCRIBE = "服务名长度：" + SERVICE_NAME_MIN + "-" + SERVICE_NAME_MAX;

    // 查询时服务名最小长度
    public static final int QUERY_SERVICE_NAME_MIN = 0;

    // 查询时服务名最大长度
    public static final int QUERY_SERVICE_NAME_MAX = 30;

    // 查询时服务名长度描述
    public static final String QUERY_SERVICE_NAME_DESCRIBE = "服务名长度：" + QUERY_SERVICE_NAME_MIN + "-" + QUERY_SERVICE_NAME_MAX;

    // 服务显示名最小长度
    public static final int SERVICE_DISPLAY_NAME_MIN = 4;

    // 服务显示名最大长度
    public static final int SERVICE_DISPLAY_NAME_MAX = 30;

    // 服务显示名描述
    public static final String SERVICE_DISPLAY_NAME_DESCRIBE = "服务显示名长度：" + SERVICE_DISPLAY_NAME_MIN + "-" + SERVICE_DISPLAY_NAME_MAX;

    // 权限名最小长度
    public static final int PERMISSION_NAME_MIN = 2;

    // 权限名最大长度
    public static final int PERMISSION_NAME_MAX = 30;

    // 权限名长度描述
    public static final String PERMISSION_NAME_DESCRIBE = "权限名称长度：" + PERMISSION_NAME_MIN + "-" + PERMISSION_NAME_MAX;

    // 查询时权限名最小长度
    public static final int QUERY_PERMISSION_NAME_MIN = 0;

    // 查询时权限名最大长度
    public static final int QUERY_PERMISSION_NAME_MAX = 30;

    // 查询时权限名长度描述
    public static final String QUERY_PERMISSION_NAME_DESCRIBE = "权限名称长度：" + PERMISSION_NAME_MIN + "-" + PERMISSION_NAME_MAX;

    // 权限访问路径最小长度
    public static final int PERMISSION_PATH_MIN = 4;

    // 权限访问路径最大长度
    public static final int PERMISSION_PATH_MAX = 45;

    // 权限访问路径描述
    public static final String PERMISSION_PATH_DESCRIBE = "权限访问路径长度：" + PERMISSION_PATH_MIN + "-" + PERMISSION_PATH_MAX;

    // 查询时权限访问路径最小长度
    public static final int QUERY_PERMISSION_PATH_MIN = 0;

    // 查询时权限访问路径最大长度
    public static final int QUERY_PERMISSION_PATH_MAX = 45;

    // 查询时权限访问路径描述
    public static final String QUERY_PERMISSION_PATH_DESCRIBE = "权限访问路径长度：" + QUERY_PERMISSION_PATH_MIN + "-" + QUERY_PERMISSION_PATH_MAX;

}

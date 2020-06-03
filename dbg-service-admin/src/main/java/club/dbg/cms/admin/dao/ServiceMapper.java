package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.ServiceDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dbg
 */
@Repository
public interface ServiceMapper {
    @Select({
            "select id, service_name, display_name, create_time, update_time, status",
            "from tb_service where id = #{id} limit 1"
    })
    ServiceDO selectServiceById(@Param("id") Integer id);

    @Select({
            "select id, service_name, display_name, create_time, update_time, status",
            "from tb_service where service_name = #{serviceName} limit 1"
    })
    ServiceDO selectServiceByName(@Param("serviceName") String serviceName);

    @Select({
            "select id, service_name, display_name from tb_service where status = 1"
    })
    List<ServiceDO> selectServiceAll();

    List<ServiceDO> selectServiceList(@Param("serviceName") String serviceName,
                                      @Param("page") Integer page, @Param("pageSize") Integer pageSize);

    int selectServiceCount(@Param("serviceName") String serviceName);

    int insertService(ServiceDO serviceData);

    int insertServices(List<ServiceDO> services);

    int updateService(ServiceDO serviceData);

    int updateServices(List<ServiceDO> services);

    int deleteService(@Param("id") Integer id);

    int deleteServices(Integer[] idArray);
}

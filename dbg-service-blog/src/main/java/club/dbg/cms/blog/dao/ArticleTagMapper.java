package club.dbg.cms.blog.dao;

import club.dbg.cms.blog.domain.ArticleTagDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTagMapper extends BaseMapper<ArticleTagDO> {

}

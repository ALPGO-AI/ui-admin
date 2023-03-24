package cc.alpgo.common.utils;

import cc.alpgo.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import cc.alpgo.common.core.page.PageDomain;
import cc.alpgo.common.core.page.TableSupport;

/**
 * 分页工具类
 *
 * @author alpgo
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            Boolean reasonable = pageDomain.getReasonable();
            PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
        }
    }
}

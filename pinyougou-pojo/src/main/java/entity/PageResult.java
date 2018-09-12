package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 对分页的信息进行一个封装，以方便进行 json 的转换
 * @author leiyu
 */
public class PageResult implements Serializable {
    /**
     * 总条数
     */
    private long total;
    /**
     * 分页条目的信息
     */
    private List rows;

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}

package com.taotao.easyui;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Item分页查询自定方法
 * 
 * @version
 */
public class EasyUIDataGridResult implements Serializable {
	private long total;
	private List rows;

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

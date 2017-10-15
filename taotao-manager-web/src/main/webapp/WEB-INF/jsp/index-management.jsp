<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<a class="easyui-linkbutton" onclick="importItem()">一键导入商品数据到数据库</a>
</div>
<script type="text/javascript">
	function importItem(){
		
		$.post("/index/import",null,function(){
			$.messager.alert('提示','商品数据导入完成!');
		});
		
	}
</script>
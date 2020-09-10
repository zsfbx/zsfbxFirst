<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="right">
        <div class="location">
            <strong>你现在所在的位置是:</strong>
            <span>用户管理页面 >> 用户添加页面</span>
        </div>
        <div class="providerAdd">
            <fm:form id="userForm" modelAttribute="user" enctype="multipart/form-data" name="userForm" method="post" action="${pageContext.request.contextPath }/user/addusersave.html">
				<input type="hidden" name="method" value="add">
                <!--div的class 为error是验证错误，ok是验证成功-->
                <div>
                    <label for="userCode">用户编码：</label>
                    <fm:input type="text" name="userCode" id="userCode" value="" path="userCode"/>
					<!-- 放置提示信息 -->
					<font color="red"></font><fm:errors path="userCode"/>
                </div>
                <div>
                    <label for="userName">用户名称：</label>
                    <fm:input type="text" name="userName" id="userName" value="" path="userName"/>
					<font color="red"></font>
                </div>
                <div>
                    <label for="userPassword">用户密码：</label>
                    <fm:input type="password" name="userPassword" id="userPassword" value="" path="userPassword"/>
					<font color="red"></font><fm:errors path="userPassword"/>
                </div>
                <div>
                    <label for="ruserPassword">确认密码：</label>
                    <input type="password" name="ruserPassword" id="ruserPassword" value=""> 
					<font color="red"></font>
                </div>
                <div>
                    <label >用户性别：</label>
					<fm:select name="gender" id="gender" path="gender">
					    <option value="1" selected="selected">男</option>
					    <option value="2">女</option>
					 </fm:select>
                </div>
                <div>
                    <label for="birthday">出生日期：</label>
                    <input type="text" Class="Wdate" id="birthday" name="birthday"
					readonly="readonly" onclick="WdatePicker();"/>
					<font color="red"></font>
                </div>
                <div>
                    <label for="phone">用户电话：</label>
                    <fm:input type="text" name="phone" id="phone" value="" path="phone"/>
					<font color="red"></font>
                </div>
                <div>
                    <label for="address">用户地址：</label>
                   <fm:input name="address" id="address"  value="" path="address"/>
                </div>
                <div>
                    <label >用户角色：</label>
                    <!-- 列出所有的角色分类 -->
					<fm:select name="userRole" id="userRole" path="userRole">
                    </fm:select>
	        		<font color="red">${uploadFileError}</font>
                </div>
                <div>
                    <label for="a_idPicPath">证件照：</label>
                    <input type="file" name="a_idPicPath" id="a_idPicPath"  value="" />
                    <font color="red">${uploadFileError}</font>
                </div>
                <div>
                    <label for="a_workPicPath">工作证照：</label>
                    <input type="file" name="a_workPicPath" id="a_workPicPath"  value="" />
                    <font color="red">${uploadWorkFileError}</font>
                </div>
                <div class="providerAddBtn">
                    <input type="button" name="add" id="add" value="保存" >
					<input type="button" id="back" name="back" value="返回" >
                </div>
            </fm:form>
        </div>
</div>
</section>
<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/useradd.js"></script>

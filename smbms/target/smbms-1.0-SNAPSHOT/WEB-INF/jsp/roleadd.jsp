<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="right">
    <div class="location">
        <strong>你现在所在的位置是:</strong>
        <span>供应商管理页面 >> 供应商添加页面</span>
    </div>
    <div class="providerAdd">
        <fm:form id="roleForm" modelAttribute="role" name="roleForm" method="post" action="${pageContext.request.contextPath }/role/addrolesave.html">
            <input type="hidden" name="method" value="add">
            <!--div的class 为error是验证错误，ok是验证成功-->
            <div class="">
                <label for="roleCode">角色编码：</label>
                <fm:input type="text" name="roleCode" id="roleCode" value="" path="roleCode"/>
                <!-- 放置提示信息 -->
                <font color="red"></font><fm:errors path="roleCode"/>
            </div>
            <div>
                <label for="roleName">角色名称：</label>
                <fm:input type="text" name="roleName" id="roleName" value="" path="roleName"/>
                <font color="red"></font><fm:errors path="roleName"/>
            </div>
            <div class="providerAddBtn">
                <input type="button" name="add" id="add" value="保存">
                <input type="button" id="back" name="back" value="返回" >
            </div>
        </fm:form>
    </div>
</div>
</section>
<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/roleadd.js"></script>

<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>

<div class="right">
     <div class="location">
         <strong>你现在所在的位置是:</strong>
         <span>订单管理页面 >> 订单添加页面</span>
     </div>
     <div class="providerAdd">
         <fm:form id="billForm" modelAttribute="bill" name="billForm" method="post" action="${pageContext.request.contextPath }/bill/addbillsave.html">
             <!--div的class 为error是验证错误，ok是验证成功-->
             <input type="hidden" name="method" value="add">
             <div class="">
                 <label for="billCode">订单编码：</label>
                 <fm:input type="text" name="billCode" class="text" id="billCode" value="" path="billCode"/>
				 <!-- 放置提示信息 -->
				 <font color="red"></font><fm:errors path="billCode"/>
             </div>
             <div>
                 <label for="productName">商品名称：</label>
                 <fm:input type="text" name="productName" id="productName" value="" path="productName"/>
				 <font color="red"></font><fm:errors path="productName"/>
             </div>
             <div>
                 <label for="productUnit">商品单位：</label>
                 <fm:input type="text" name="productUnit" id="productUnit" value="" path="productUnit"/>
				 <font color="red"></font><fm:errors path="productUnit"/>
             </div>
             <div>
                 <label for="productCount">商品数量：</label>
                 <fm:input type="text" name="productCount" id="productCount" value="" path="productCount"/>
				 <font color="red"></font><fm:errors path="productCount"/>
             </div>
             <div>
                 <label for="totalPrice">总金额：</label>
                 <fm:input type="text" name="totalPrice" id="totalPrice" value="" path="totalPrice"/>
				 <font color="red"></font><fm:errors path="totalPrice"/>
             </div>
             <div>
                 <label >供应商：</label>
                 <fm:select name="providerId" id="providerId" path="providerId">
		         </fm:select>
				 <font color="red"></font>
             </div>
             <div>
                 <label >是否付款：</label>
                 <input type="radio" name="isPayment" value="1" checked="checked"/>未付款
				 <input type="radio" name="isPayment" value="2" >已付款
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
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/billadd.js"></script>
var roleCode = null;
var roleName = null;
var saveBtn = null;
var backBtn = null;

$(function(){
    roleCode = $("#roleCode");
    roleName = $("#roleName");
    saveBtn = $("#save");
    backBtn = $("#back");

    //初始化的时候，要把所有的提示信息变为：* 以提示必填项，更灵活，不要写在页面上
    roleCode.next().html("*");
    roleName.next().html("*");




    saveBtn.on("click",function(){
        // proContact.blur();
        // proPhone.blur();
        // if(proContact.attr("validateStatus") == "true" &&
        // 		proPhone.attr("validateStatus") == "true"){
        if(confirm("是否确认提交数据")){
            $("#roleForm").submit();
        }
        // }
    });

    backBtn.on("click",function(){
        //alert("modify: "+referer);
        if(referer != undefined
            && null != referer
            && "" != referer
            && "null" != referer
            && referer.length > 2){
            window.location.href = referer;
        }else{
            history.back(-1);
        }
    });
});
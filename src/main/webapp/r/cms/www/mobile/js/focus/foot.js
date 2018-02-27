//导航
function myNav(){
	if($("#nav").hasClass("openNav")){
		$("#nav-over").css("display","none");
		$("#nav").removeClass("openNav");
		$("#warmp,.footer-con").removeClass("openMenu");
	}else{
		$("#nav-over").css("display","block");
		$("#nav").addClass("openNav");
		$("#warmp,.footer-con").addClass("openMenu");
				
		$("#scrollerBox").height($(window).height() - $("#nav h3").outerHeight());
		//new IScroll('#scrollerBox',{preventDefault:false});		
		$(window).resize(function(){
			$("#scrollerBox").height($(window).height() - $("#nav h3").outerHeight());
		})
	}	
}
//项目导航
function ksNav(){
	if($("#ks-nav").hasClass("openNav")){
		$("#nav-over").css("display","none");
		$("#ks-nav").removeClass("openNav");
		$("#warmp,.footer-con").removeClass("openMenu")	
	}else{
		$("#nav-over").css("display","block");
		$("#ks-nav").addClass("openNav");
		$("#warmp,.footer-con").addClass("openMenu");
		
		
		$("#ks-scrollerBox").height($(window).height() - $("#ks-nav h3").outerHeight());
		//new IScroll('#ks-scrollerBox',{preventDefault:false});		
		$(window).resize(function(){
			$("#ks-scrollerBox").height($(window).height() - $("#nav h3").outerHeight());
		})
	}
}
$("#nav-over").bind("click",function(){
	$("#nav-over").css("display","none");
	$("#warmp,.footer-con").removeClass("openMenu");
	$("#nav").removeClass("openNav");
	$("#ks-nav").removeClass("openNav");
	$("#warmp,.footer-con").removeClass("openMenu")	
})
$("#nav-over").bind("touchmove touch",function(e){e.preventDefault()},false);//阴止默认事件
$(".navHome").bind("click",myNav);
$(".navIteam").bind("click",ksNav);

//焦点图
TouchSlide({ 
	slideCell:"#slides",
	titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
	mainCell:".bd ul", 
	effect:"leftLoop", 
	autoPage:true,//自动分页
	autoPlay:true //自动播放
});


//返回顶部
$("body").append('<div class="gotop" id="gotop"><div>');
$(window).scroll(function(){$(document).scrollTop()>300?$("#gotop").fadeIn():$("#gotop").fadeOut()});
$("#gotop").click(function(){$("html,body").animate({scrollTop:0},300)})
//标题文字滚动
//function run(obj){
//	var obj = document.getElementById(obj);
//	var strText= obj.innerHTML;
//	strText=strText.substring(1,strText.length)	+ strText.substring(0,1);
//	obj.innerHTML = strText;	  
//}
//setInterval('run("title")',400);
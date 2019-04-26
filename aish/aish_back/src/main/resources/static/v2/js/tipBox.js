(function() {
	$.MsgBox = {
		Alert: function(title, msg, cicon, callback) {
			GenerateHtml("alert", title, msg,cicon);
			btnOk(callback);
			btnNo();
		},
		Confirm: function(title, msg, cicon, callback) {
			GenerateHtml("confirm", title, msg,cicon);
			btnOk(callback);
			btnNo();
		},
		Nobutton: function(title, msg, cicon, callback) {
			GenerateHtml("nobutton", title, msg);
		}
	}
	//生成Html  
	var GenerateHtml = function(type, title, msg,cicon) {
		
		var _html = "";
		_html += '<div id="tipBox"><div id="tipBox-con"><div id="tipBox-tit" class="clearfix"><span id="tit-l">' + title + '</span><span id="tipBox-ico"></span></div>';
		_html += '<div id="tipBox-content"><i class="icon ' + cicon + '"></i>' + msg + '</div><div id="tipBox-btnbox">';
		if(type == "alert") {
			_html += '<input id="tipBox-btn-ok" type="button" value="确定" />';
		}  
		if(type == "confirm") {
			_html += '<input id="tipBox-btn-no" type="button" value="取消" />';
			_html += '<input id="tipBox-btn-ok" type="button" value="确定" />';
		}
		if(type == "Nobutton") {}
		_html += '</div></div></div>';
		$("body").append(_html);
		GenerateCss();
	}

	//生成Css  
	var GenerateCss = function() {
		$("#tipBox").css({
			width: '100%',
			height: '100%',
			zIndex: '99999',
			position: 'fixed',
			background: 'rgba(0,0,0,0.6)',
			top: '0',
			left: '0',
			display:"flex",
			alignItems: 'center',
			justifyContent: 'center',
		});
		$("#tipBox-con").css({
			zIndex: '999999',
			paddingBottom:'45px',
        	background:'#fff',
		});
		$("#tipBox-tit").css({
			display: 'block',
			height:'50px',
            lineHeight:'50px',
            padding:'0 26px',
            background:'#fdfdfd',
            borderBottom:'1px solid #ebebeb',
		});
		$("#tit-l").css({
			float:'left',
            fontSize:'14px',
            color:'#666',
		});
		$("#tipBox-ico").css({
			display: 'block',
			float:'right',
			width:'19px',
			height:'17px',
			marginTop:'15px',
            background:'url(/v2/img/close.png) no-repeat',
            cursor: 'pointer'
		});
		$("#tipBox-content").css({
			fontSize:'14px',
			padding: '60px 100px',
			lineHeight: '20px',
			color:'#999',
			textAlign: "center"
		});
		$("#tipBox-btnbox").css({ margin: '15px 0 10px 0', textAlign: 'center',padding:'0 100px' });
		$("#tipBox-btn-ok,#tipBox-btn-no").css({ width: '102px', height: '40px', fontSize:'14px', color: '#fff', border: 'none',background:'#4876e6',borderRadius:'3px',margin:'0 auto' });
		$("#tipBox-btn-no").css({ float:'left', color: '#989898', backgroundColor: '#fff', border:'1px solid #dbdbdb', marginRight: '15px' });
		//关闭按钮事件
		$("#tipBox-ico").click(function() {
			$("#tipBox,#tipBox-con").remove();
		});
		var _widht = document.documentElement.clientWidth; //屏幕宽  
		var _height = document.documentElement.clientHeight; //屏幕高  
		var boxWidth = $("#tipBox-con").width();
		var boxHeight = $("#tipBox-con").height();
		//让提示框居中  
		$("#mb_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });
	}
	
	//确定按钮事件  
	var btnOk = function(callback) {
		$("#tipBox-btn-ok").click(function() {
			$("#tipBox,#tipBox-con").remove();
			if(typeof(callback) == 'function') {
				callback();
			}
		});
	}
	//取消按钮事件  
	var btnNo = function() {
		$("#tipBox-btn-no,#tipBox-ico").click(function() {
			$("#tipBox,#tipBox-con").remove();
		});
	}

})();
(function() {
    $.MsgBox = {
        Alert: function(title, msg, callback) {
            GenerateHtml("alert", title, msg);
            btnOk(callback);
            btnNo();
        },
        Confirm: function(title, msg, callback) {
            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            btnNo();
        }
    }
    //生成Html
    var GenerateHtml = function(type, title, msg) {
        var _html = "";
        _html += '<div id="mb_box"><div id="mb_con"><span id="mb_tit">' + title + '</span>';
        _html += '<div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        if(type == "alert") {
            _html += '<input id="mb_btn_ok1" class="sureBtn" type="button" value="确定" />';
        }
        if(type == "confirm") {
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        _html += '</div></div></div>';
        $("body").append(_html);
        GenerateCss();
    }

    //生成Css
    var GenerateCss = function() {
        $("#mb_box").css({
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
        $("#mb_con").css({
            zIndex: '999999',
            width: '70%',
            backgroundColor: 'White',
            borderRadius: '15px'
        });
        $("#mb_tit").css({
            display: 'block',
            fontSize: '.3rem',
            textAlign: "center",
            color: '#444',
            padding: '10px 15px',
            borderRadius: '15px 15px 0 0',
            fontWeight: 'bold'
        });
        $("#mb_msg").css({
            fontSize:'15px',
            padding: '20px',
            lineHeight: '20px',
            color:'#333',
            borderBottom: '1px solid #c5c5c5',
            textAlign: "center"
        });
        $("#mb_ico").css({
            display: 'block',
            position: 'absolute',
            right: '10px',
            top: '9px',
            border: '1px solid Gray',
            width: '20px',
            height: '20px',
            textAlign: 'center',
            lineHeight: '16px',
            cursor: 'pointer',
            fontSize: "20px",
            lineHeight: "20px",
            borderRadius: '12px',
            fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({ textAlign: 'center', lineHeight: '0' });
        $("#mb_btn_ok,#mb_btn_no").css({ width: '110px', padding:'15px 0', fontSize:'16px', color: '#007aff' });
        $("#mb_btn_ok").css({borderLeft: '1px solid #c5c5c5'});
        $("#mb_btn_ok1").css({width: '100%',padding:'25px 0',fontSize:'16px', color: '#007aff',background: 'none',border: 'none'});
        $("#mb_btn_no").css({  });
        $("#mb_ico").hover(function() {
            $(this).css({ backgroundColor: 'Red', color: 'White' });
        }, function() {
            $(this).css({ backgroundColor: '#DDD', color: 'black' });
        });
        var _widht = document.documentElement.clientWidth; //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();
        /*//让提示框居中
        $("#mb_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });*/
    }
    //确定按钮事件
    var btnOk = function(callback) {
        $("#mb_btn_ok").click(function() {
            $("#mb_box,#mb_con").remove();
            if(typeof(callback) == 'function') {
                callback();
            }
        });
        $("#mb_btn_ok1").click(function() {
            $("#mb_box,#mb_con").remove();
            if(typeof(callback) == 'function') {
                callback();
            }
        });
    }
    //取消按钮事件
    var btnNo = function() {
        $("#mb_btn_no,#mb_ico").click(function() {
            $("#mb_box,#mb_con").remove();
        });
    }

})();
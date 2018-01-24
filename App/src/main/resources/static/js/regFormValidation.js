$(document).ready(function(){

    $("#passVal").hide();
    $("#repassVal").hide();
    $("button").addClass("disabled");

    $("input[type=password]").keyup(function(){
        var ucase = new RegExp("[A-Z]+");
        var lcase = new RegExp("[a-z]+");
        var num = new RegExp("[0-9]+");
        var lB = 0;
        var mB = 0;
        var nB = 0;
        var rB = 0;


        if($("#password").val().length >= 8){
            $("#8char").removeClass("glyphicon-remove");
            $("#8char").addClass("glyphicon-ok");
            $("#8char").css("color","#00A41E");
            lB = 1;
        }else{
            $("#8char").removeClass("glyphicon-ok");
            $("#8char").addClass("glyphicon-remove");
            $("#8char").css("color","#FF0004");
            lB = 0;
        }

        if(lcase.test($("#password").val())){
            $("#lcase").removeClass("glyphicon-remove");
            $("#lcase").addClass("glyphicon-ok");
            $("#lcase").css("color","#00A41E");
            mB = 1;
        }else{
            $("#lcase").removeClass("glyphicon-ok");
            $("#lcase").addClass("glyphicon-remove");
            $("#lcase").css("color","#FF0004");
            mB = 0;
        }

        if(num.test($("#password").val())){
            $("#num").removeClass("glyphicon-remove");
            $("#num").addClass("glyphicon-ok");
            $("#num").css("color","#00A41E");
            nB = 1;
        }else{
            $("#num").removeClass("glyphicon-ok");
            $("#num").addClass("glyphicon-remove");
            $("#num").css("color","#FF0004");
            mB = 0;
        }

        if($("#password").val() == $("#repasswor").val()){
            $("#pwmatch").removeClass("glyphicon-remove");
            $("#pwmatch").addClass("glyphicon-ok");
            $("#pwmatch").css("color","#00A41E");
            rB = 1;
        }else{
            $("#pwmatch").removeClass("glyphicon-ok");
            $("#pwmatch").addClass("glyphicon-remove");
            $("#pwmatch").css("color","#FF0004");
            rB = 0;
        }

        if(Boolean(rB) && Boolean(mB) && Boolean(nB) && Boolean(lB)){
            $("button").removeClass("disabled");
        } else {
            $("button").addClass("disabled");
        }


    }).focus(function(){ $("#passVal").show();
        $("#repassVal").show();})
        .blur(function(){ $("#passVal").hide();
            $("#repassVal").hide();});

});
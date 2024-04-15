$(document).ready(function(){
    var gnb = $('#gnb > li > a');

    gnb.click(function(e){
        e.preventDefault();
        
        var isOpen = $(this).next().is(':visible');

        if(isOpen){
            // 현재 서브메뉴를 닫음
            $(this).next().slideUp(200);
        }else{
            // 현재 서브메뉴를 펼침
            $(this).next().slideDown(200);
        }
    });
});
$(function() {
    $("#tabs").tabs();

    // 팝업 닫기
    $('.btnClose').click(function(){
        $(this).closest('.popup').removeClass('on');
    });

    // 배너등록 팝업 띄우기
    $('.btnRegister').click(function(e){
        e.preventDefault();
        $('#bannerRegister').addClass('on');
    });
});
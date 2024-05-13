$(function(){

    
    // 판매자 정보 출력
    async function getSellerInfo(companyTag) {
        const companyName = document.getElementsByClassName('companyName')[0];
        const ceo = document.getElementsByClassName('ceo')[0];
        const tel = document.getElementsByClassName('tel')[0];
        const fax = document.getElementsByClassName('fax')[0];
        const email = document.getElementsByClassName('ceo')[0];
        const bizNum = document.getElementsByClassName('bizNum')[0];
        const address = document.getElementById('address');

        const company = companyTag.innerText;

        const jsonData = {
            "company": company
        };

        try {
            const response = await fetch('/lotteon/my/sellerInfo', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(jsonData)
            });
            const data = await response.json();

            companyName.innerHTML = "";
            companyName.textContent = data.company;

            ceo.innerHTML = "";
            ceo.textContent = data.ceo;

            tel.innerHTML = "";
            tel.textContent = data.tel;

            fax.innerHTML = "";
            fax.textContent = data.fax;

            email.innerHTML = "";
            email.textContent = data.email;

            bizNum.innerHTML = "";
            bizNum.textContent = data.bizRegNum;

            address.innerHTML = "";
            address.textContent = '[' + data.zip + ']' + data.addr1 + ' ' + data.addr2;
        } catch (err) {
            console.log(err);
        }
    }

    $('.info > .company a').click(async function (e) {
        e.preventDefault();
        const companyTag = $(this).closest('.company')[0];
        console.log(companyTag);

        await getSellerInfo(companyTag);

        $('#popSeller').addClass('on');
    });


    // 문의하기 팝업 띄우기
    $('.btnQuestion').click(function(e){
        e.preventDefault();




        $('.popup').removeClass('on');
        $('#popQuestion').addClass('on');
    });

    // 주문상세 팝업 띄우기
    $('.latest .info .orderNo > a').click(function(e){
        e.preventDefault();
        $('#popOrder').addClass('on');
    });

    // 수취확인 팝업 띄우기
    $('.latest .confirm > .receive').click(function(e){
        e.preventDefault();
        $('#popReceive').addClass('on');
    });

    // 상품평 작성 팝업 띄우기
    $('.latest .confirm > .review').click(function(e){
        e.preventDefault();
        $('#popReview').addClass('on');
    });

    // 팝업 닫기
    $('.btnClose').click(function(){
        $(this).closest('.popup').removeClass('on');
    });

    // 팝업 닫기
    $('.btnCancel').click(function(e){
        e.preventDefault();
        $(this).closest('.popup').removeClass('on');
    });

    // 상품평 작성 레이팅바 기능
    $(".my-rating").starRating({
        starSize: 20,
        useFullStars: true,
        strokeWidth: 0,
        useGradient: false,
        minRating: 1,
        ratedColors: ['#ffa400', '#ffa400', '#ffa400', '#ffa400', '#ffa400'],
        callback: function(currentRating, $el){
            alert('rated ' + currentRating);
            console.log('DOM element ', $el);
        }
    });

    // info - 비밀번호 변경 창 띄우기
    $('#btnPassChange').click(function(e){
        e.preventDefault();
        $('#popPassChange').addClass('on');
    });

    // info - 이메일 변경 창 띄우기
    $('#btnEmailChange').click(function(e){
        e.preventDefault();
        $('#popEmailChange').addClass('on');
    });

    // info - 회원 탈퇴 창 띄우기
    $('#btnWithdraw').click(function(e){
        e.preventDefault();
        $('#popWithdraw').addClass('on');
    });

});
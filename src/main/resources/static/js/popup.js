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

        const prodNos = $(this).closest('.info')[0];
        prodNo = prodNos.querySelector('input[type="hidden"]').value;

        await getSellerInfo(companyTag);

        $('#popSeller').addClass('on');
    });


    // 문의하기 팝업 띄우기
    $('.btnQuestion').click(function(e){
        e.preventDefault();
        $('.popup').removeClass('on');
        $('#popQuestion').addClass('on');
    });


    const accordion = document.getElementsByClassName('accordion-item');
    for(let i=0; i< accordion.length; i++) {
        const ordTag = '#ordNo' + i;
        const popOrderId = '#popOrder' + i;
        const uidId = '.uid' + i;
        const ordNoId = '.ordNos' + i;


        console.log(ordTag)
        console.log(popOrderId)
        console.log(uidId)
        console.log(ordNoId)

        // 주문상세 팝업 띄우기
        $('.latest .accordion-item .accordion-button .ordNo a').click(function (e) {
            e.preventDefault();
            $('#popOrder').addClass('on');


        });
    }








    // 수취확인 팝업 띄우기
    const accordions = document.getElementsByClassName('accordion-item');
    for(let i=0; i< accordions.length; i++){
        const receiveId = '#receive'+i;
        const popReceiveId = '#popReceive'+i;
        const prodNoId = '.prodNo'+i;
        const ordNoId = '.ordNo'+i;
        const uidId = '.uid'+i;
        const reviewCheck='#reviewCheck'+i;


        $(reviewCheck).click(async function (e) {

            e.preventDefault();

            const prodNo = document.querySelector(prodNoId).value;
            console.log('제품번호' + prodNo);

            const ordNo = document.querySelector(ordNoId).value;
            console.log('주문번호' + ordNo);

            const uid = document.querySelector(uidId).value;
            console.log('고객아이디' + uid);

            const jsonData = {
                "prodNo": prodNo,
                "ordNo": ordNo,
                "uid": uid
            }
            console.log(jsonData);

            await fetch('/lotteon/my/reviewCheck', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(jsonData)
            }).then(response => response.text())
                .then(data => {
                    if (data == 'success') {
                        alert('구매확정이 완료되었습니다.')
                        location.href = `/lotteon/my/home?uid=${uid}`;

                    } else {
                        alert('리뷰작성해주세요');
                        $('#popReview').addClass('on');
                    }
                })
        })

        $('.latest .confirm >' +receiveId).click(async function (e) {
            e.preventDefault();

            $(popReceiveId).addClass('on');
        })

    }

        // 상품평 작성 팝업 띄우기
        $('.latest .confirm > .review').click(function (e) {
            e.preventDefault();
            $('#popReview').addClass('on');
        });

        // 팝업 닫기
        $('.btnClose').click(function () {
            $(this).closest('.popup').removeClass('on');
        });

        // 팝업 닫기
        $('.btnCancel').click(function (e) {
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
            callback: function (currentRating, $el) {
                alert('rated ' + currentRating);
                console.log('DOM element ', $el);
            }
        });

        // info - 비밀번호 변경 창 띄우기
        $('#btnPassChange').click(function (e) {
            e.preventDefault();
            $('#popPassChange').addClass('on');
        });

        // info - 이메일 변경 창 띄우기
        $('#btnEmailChange').click(function (e) {
            e.preventDefault();
            $('#popEmailChange').addClass('on');
        });

        // info - 회원 탈퇴 창 띄우기
        $('#btnWithdraw').click(function (e) {
            e.preventDefault();
            $('#popWithdraw').addClass('on');
        });

    });
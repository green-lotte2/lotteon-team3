window.onload = function() {

    let isPassOk = false;
    let isNickOk = false;
    let isEmailOk = false;
    let isChangeEmailOk=false;
    let isEmailCodeOk = false;
    let isPassCheckOk=false;
    let isHpOk = false;


    const rePass = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
    const reNick = /^[a-zA-Zㄱ-힣0-9]{2,5}$/;
    const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    const reHp = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;


    const btnComplete = document.querySelector('.btnComplete');
    const btnPassCheck = document.querySelector('.btnPassCheck');

    // 나의설정 비밀번호 재확인
    if (btnPassCheck) {
        btnPassCheck.addEventListener('click', async function (e) {
            e.preventDefault();

            const inputPass = document.querySelector('input[name=pass1]').value;
            const uid = document.querySelector('input[name=uid]').value;

            const jsonData = {
                "uid": uid,
                "pass": inputPass,
            };
            console.log(jsonData);

            await fetch('/lotteon/my/infoAccessCheck', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(jsonData)
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    if (data) {
                        isPassCheckOk=true;

                        if(isPassCheckOk) {
                            location.href = `/lotteon/my/info?uid=${uid}`;
                        }

                    } else {
                        alert("비밀번호가 올바르지 않습니다.");
                        isPassCheckOk=false;
                    }

                })
                .catch((err) => {
                    console.log(err);
                    alert("비밀번호가 올바르지 않습니다.");
                    isPassCheckOk=false;
                });
        });
    }
    // 비밀번호 변경
    const inputPass1 = document.querySelector('input[name=pass1]');
    const inputPass2 = document.querySelector('input[name=pass2]');
    const resultPass = document.getElementById('result_pass');

    inputPass2.addEventListener('focusout', () => {

            if (inputPass1.value === inputPass2.value) {
                if (!inputPass1.value.match(rePass)) {
                    alert('비밀번호 형식이 맞지 않습니다.')
                    isPassOk = false;
                } else {
                    isPassOk = true;
                    if (btnComplete) {
                        if (isPassOk) {
                            btnComplete.addEventListener('click', async function (e) {
                                console.log("최종 비밀번호" + isPassOk);

                                const uid = document.querySelector('input[name=uid]').value;

                                const jsonData = {
                                    "uid": uid,
                                    "pass": inputPass1.value,
                                };

                                console.log("아이디" + uid);
                                console.log("비밀번호" + inputPass1.value);

                                await fetch('/lotteon/my/formMyinfoPassChange', {
                                    method: 'POST',
                                    headers: {'Content-Type': 'application/json'},
                                    body: JSON.stringify(jsonData)
                                })
                                    .then(response => response.text())
                                    .then(data => {
                                        console.log(data);
                                        if (data === "success") {
                                            alert('비밀번호가 변경되었습니다. \n다시 로그인 해주세요.');
                                            isPassOk = true;
                                            document.getElementById('popPassChange').closest('.popup').classList.remove('on');
                                            location.href = '/lotteon/member/logout';
                                        } else {
                                            alert("비밀번호 변경에 실패했습니다.");
                                        }
                                    })
                                    .catch(error => {
                                        console.log(error)
                                        alert("비밀번호가 올바르지 않습니다.");
                                    });

                            });

                        } else {
                            alert('비밀번호를 확인해주세요.')
                            isPassOk = false;
                        }
                    }


                }

            }else {
                alert('비밀번호가 일치하지않습니다.')
                isPassOk = false;
            }
            console.log("비밀번호" + isPassOk);
        }
    )



    // 닉네임 변경
    const btnNickChange = document.getElementById('btnNickChange');
    const resultNick = document.getElementById('result_nick');
    const inputNick = document.querySelector('input[name=nick]');


    inputNick.addEventListener('focusout', () => {

        if (!inputNick.value.match(reNick)) {
            resultNick.innerText = '닉네임 형식이 맞지 않습니다.';
            resultNick.style.color = 'red';
            isNickOk = false;

        } else {
            resultNick.innerText = '';
            isNickOk = true;

            if (btnNickChange && isNickOk) {
                btnNickChange.addEventListener('click', async function (e) {

                    const confirmMessage = '닉네임을 변경하시겠습니까?';
                    const isConfirmed = confirm(confirmMessage);

                    const uid = document.querySelector('input[name=uid]').value;
                    if (isConfirmed) {
                        const jsonData = {
                            "uid": uid,
                            "nick": inputNick.value,
                        };

                        console.log("아이디" + uid);
                        console.log("닉네임" + inputNick.value);

                        await fetch('/lotteon/my/formMyinfoNickChange', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify(jsonData)

                        })
                            .then(response => response.text())
                            .then(data => {
                                console.log(data);
                                if (data === "success") {
                                    alert('닉네임이 변경되었습니다.');
                                    location.href = `/lotteon/my/info?uid=${uid}`;
                                } else {
                                    alert('이미 사용중인 닉네임입니다.');
                                }
                            })
                            .catch(error => {
                                console.log(error)
                            });
                    } else {
                        // 사용자가 취소를 선택한 경우
                        alert('닉네임 변경이 취소되었습니다.');
                    }


                })


            }
        }
    })

    // 이메일 변경 - 등록된 이메일 입력
    const btnEmailComplete=document.querySelector('.btnEmailComplete');
    const btnEmailChange = document.querySelector('.btnEmailChange');
    const btnCheckOriginEmail=document.querySelector('.btnCheckOriginEmail');
    const checkOriginEmail_result=document.getElementById('checkOriginEmail_result');
    const changeEmail = document.querySelector('input[name=changeEmail]'); // 수정 이메일
    const checkOriginEmail = document.querySelector('input[name=checkOriginEmail]'); // 기존 이메일
    const uid = document.querySelector('input[name=uid]').value;
    
    // 이메일 변경 - 이메일 변경하기 클릭
    // 1) 등록된 이메일 일치 검사
    if(btnCheckOriginEmail){
        btnCheckOriginEmail.addEventListener('click',async function(e){

            e.preventDefault();
            console.log('내가 입력한 이메일 : '+checkOriginEmail.value);


            const jsonData = {
                "uid": uid,
                "email": checkOriginEmail.value,
            };
            console.log(jsonData);

            await fetch('/lotteon/my/checkOriginEmail', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(jsonData)
            })
                .then(response => response.text())
                .then(data => {
                    console.log(data);
                    if (data==="success") {
                        checkOriginEmail_result.innerText='등록된 정보가 확인되었습니다.';
                        changeEmail.focus();
                        isChangeEmailOk=true;

                    } else {
                        alert("등록된 정보와 일치하지않습니다.");
                        isChangeEmailOk=false;
                    }

                })
                .catch((err) => {
                    console.log(err);
                });
        })
        
    }


    const btnCheckChangeEmail=document.querySelector('.btnCheckChangeEmail');
    const checkChangeEmail_result=document.getElementById('checkChangeEmail_result');
    const inputEmailCode = document.getElementById('inputEmailCode');

    if(btnCheckChangeEmail){
        btnCheckChangeEmail.addEventListener('click',async function(e) {
            console.log("이메일" + isChangeEmailOk);

            const type = this.dataset.type;
            console.log("type : " + type);
            console.log("value : "+changeEmail.value);

            // 유효성 검사
            if (!changeEmail.value.match(reEmail)) {

                checkChangeEmail_result.innerText = '이메일 형식이 맞지 않습니다.';
                checkChangeEmail_result.style.color = 'red';
                isEmailOk = false;
            } else { //유효성 검사 후 중복검사

                await fetch(`/lotteon/member/check/${type}/${changeEmail.value}`)
                    .then(response => response.json())
                    .then(data => {
                        console.log(data);
                        if (data.result > 0) {
                            checkChangeEmail_result.innerText = '이미 사용중인 이메일입니다.';
                            checkChangeEmail_result.style.color = 'red';
                            isEmailOk = false;

                        } else {
                            checkChangeEmail_result.innerText = '인증코드 전송이 완료되었습니다. 이메일을 확인해주세요.';
                            checkChangeEmail_result.style.color = 'black';
                            // 이메일 인증번호 입력칸 활성화
                            inputEmailCode.removeAttribute('disabled');
                            // 이메일 인증번호를 입력할 수 있는 input에 포커스 설정
                            inputEmailCode.focus();
                            isEmailOk = true;
                        }


                    })
                    .catch((err) => {
                        console.log(err);
                    });


            }
        })
    }

    // 이메일 인증코드 확인
    const btnCheckEmailCode = document.getElementById('btnCheckEmailCode');
    const resultEmailCode = document.getElementById('resultEmailCode');

    if(btnCheckEmailCode) {
        btnCheckEmailCode.addEventListener('click', async function (e) {

            console.log('이메일 인증코드 : '+inputEmailCode.value)
            await fetch(`/lotteon/member/email/${inputEmailCode.value}`)
                .then(response => response.json())
                .then(data => {
                    console.log('인증코드 일치 결과 :'+data.result);
                    if (!data.result) {
                        resultEmailCode.innerText = '인증코드가 일치하지 않습니다.';
                        resultEmailCode.style.color = 'red';
                        isEmailCodeOk = false;
                    } else {
                        resultEmailCode.innerText = '이메일이 인증되었습니다.';
                        resultEmailCode.style.color = 'green';
                        isEmailCodeOk = true;
                    }


                })
                .catch((err) => {
                    console.log(err);
                });

        })
    }

    if(btnEmailComplete){
        btnEmailComplete.addEventListener('click', async function (e) {

            console.log('이메일 수정 최종');
            console.log('수정 이메일 ' +isChangeEmailOk);
            console.log('이메일 ' +isEmailOk);
            console.log('이메일 인증번호 ' +isEmailCodeOk);



            const jsonData={
                "uid":uid,
                "email":changeEmail.value
            }
            console.log(jsonData);

            if(isEmailOk&&isEmailCodeOk&&isChangeEmailOk) {

                await fetch('/lotteon/my/formMyinfoEmailChange', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(jsonData)
                })
                    .then(response => response.text())
                    .then(data => {
                        console.log(data);
                        if (data === "success") {
                            alert('이메일이 변경되었습니다.');
                            document.getElementById('popEmailChange').closest('.popup').classList.remove('on');
                            location.href = `/lotteon/my/info?uid=${uid}`;

                        } else {
                            alert("이메일을 확인해주세요.");
                        }

                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }else{
                alert("이메일을 확인해주세요.")
            }
        })
    }

    // 휴대폰 변경
    const inputHp = document.querySelector('input[name=hp]');
    const btnHpChange=document.getElementById('btnHpChange');
    const resultHp=document.getElementById('result_hp')

    inputHp.addEventListener('focusout',()=>{

        if(!inputHp.value.match(reHp)){
            resultHp.innerText = '휴대폰 형식이 맞지 않습니다.';
            resultHp.style.color = 'red';
            isHpOk = false;
        }else{
            resultHp.innerText='';
            isHpOk=true;
            if(btnHpChange&&isHpOk){
                btnHpChange.addEventListener('click', async function (e) {

                    const confirmMessage = '휴대폰을 변경하시겠습니까?';
                    const isConfirmed = confirm(confirmMessage);

                    if (isConfirmed) {
                        const jsonData = {
                            "uid": uid,
                            "hp": inputHp.value,
                        };

                        console.log("아이디" + uid);
                        console.log("휴대폰" + inputHp.value);

                        await fetch('/lotteon/my/formMyinfoHpChange', {
                            method: 'POST',
                            headers: {'Content-Type': 'application/json'},
                            body: JSON.stringify(jsonData)

                        })
                            .then(response => response.text())
                            .then(data => {
                                console.log(data);
                                if (data === "success") {
                                    alert('휴대폰이 변경되었습니다.');
                                    location.href = `/lotteon/my/info?uid=${uid}`;
                                } else {
                                    alert('이미 사용중인 휴대폰입니다.');
                                }
                            })
                            .catch(error => {
                                console.log(error)
                            });
                    } else {
                        // 사용자가 취소를 선택한 경우
                        alert('휴대폰 변경이 취소되었습니다.');
                    }


                })

            }
        }

    })


    const inputZip=document.getElementsByName('zip')[0];
    const inputAddr1=document.getElementById("inputAddr1");
    const inputAddr2= document.getElementById("inputAddr2");
    const btnAddrChange=document.getElementById('btnAddrChange');

    //우편번호 검색
    function postcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                inputZip.value = data.zonecode;
                inputAddr1.value = addr;
                // 커서를 상세주소 필드로 이동한다.
                inputAddr2.value = "";
                inputAddr2.focus();
            }
        }).open();
    }

    findZip.onclick = function () {
        postcode();
    }

    // 주소 수정
    if(btnAddrChange){

        btnAddrChange.addEventListener('click',async function(e){
            const confirmMessage = '주소를 변경하시겠습니까?';
            const isConfirmed = confirm(confirmMessage);

            console.log("우편번호 : "+inputZip.value);
            console.log("주소 : "+inputAddr1.value);
            console.log("상세주소 : "+inputAddr2.value);

            if (isConfirmed) {

                const jsonData = {

                    "uid": uid,
                    "zip": inputZip.value,
                    "addr1": inputAddr1.value,
                    "addr2": inputAddr2.value,
                };

                console.log(jsonData);

                await fetch('/lotteon/my/formMyinfoAddrChange', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(jsonData)
                })
                    .then(response => response.text())
                    .then(data => {
                        console.log(data);
                        if (data === "success") {
                            alert('주소 수정이 완료되었습니다.');
                            location.href = `/lotteon/my/info?uid=${uid}`;
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }else{
                alert('주소 변경이 취소되었습니다.');
            }


            })

    }

    //탈퇴하기
    const btnWithdraw=document.getElementById('btnWithdraw'); // 탈퇴 버튼
    const btnPopWithdraw=document.querySelector('.btnPopWithdraw');

    if(btnWithdraw){
        btnWithdraw.addEventListener('click' ,function(e){

            let result=confirm("탈퇴를 희망하시는게 맞나요?")
            if(result){
                document.getElementById('popWithdraw').classList.add('on');
                btnPopWithdraw.addEventListener('click',async function (e) {

                    const inputPass = document.querySelector('input[name=passCheck]');

                    const jsonData={
                        "uid":uid,
                        "pass":inputPass.value
                    }


                    await fetch('/lotteon/my/withdraw', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify(jsonData)
                    }).then(response => response.text())
                        .then(data => {
                            console.log(data);
                            if (data === "success") {
                                alert('탈퇴가 완료되었습니다.');
                                location.href = "/lotteon/member/logout";
                            }else{
                                alert('비밀번호가 일치하지 않습니다.');
                            }
                        })
                        .catch((err) => {
                            console.log(err);
                        });
                })
            }else{
                alert('탈퇴가 취소되었습니다.');
            }


        })
    }





}//끝







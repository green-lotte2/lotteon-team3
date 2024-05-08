window.onload = function() {

    let isPassOk=false;
    let isNickOk  = false;

    const rePass = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
    const reNick  = /^[a-zA-Zㄱ-힣0-9]{2,5}$/;
    const btnComplete = document.querySelector('.btnComplete');
    const btnPassCheck = document.querySelector('.btnPassCheck');

    if(btnPassCheck) {
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
                        location.href = `/lotteon/my/info?uid=${uid}`;
                    } else {
                        alert("비밀번호가 올바르지 않습니다.");
                    }

                })
                .catch((err) => {
                    console.log(err);
                    alert("비밀번호가 올바르지 않습니다.");
                });
        });
    }

    const inputPass1 = document.querySelector('input[name=pass1]');
    const inputPass2 = document.querySelector('input[name=pass2]');
    const resultPass = document.getElementById('result_pass');

    inputPass2.addEventListener('focusout',()=>{

        if (inputPass1.value === inputPass2.value) {
            if (!inputPass1.value.match(rePass)) {
                alert('비밀번호 형식이 맞지 않습니다.')
                isPassOk = false;
            } else {

                isPassOk = true;

                if(btnComplete) {
                    btnComplete.addEventListener('click', async function (e) {
                        console.log("최종 비밀번호"+isPassOk);

                        const uid = document.querySelector('input[name=uid]').value;

                        const jsonData={
                            "uid":uid,
                            "pass":inputPass1.value,
                        };

                        console.log("아이디"+uid);
                        console.log("비밀번호"+inputPass1.value);

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
                }
            }
        } else {
            alert('비밀번호가 일치하지않습니다.')
            isPassOk = false;
        }
        console.log("비밀번호"+isPassOk);
    });

    const btnNickChange=document.getElementById('btnNickChange');
    const resultNick=document.getElementById('result_nick');
    const inputNick = document.querySelector('input[name=nick]');



    inputNick.addEventListener('focusout',()=>{

        if(!inputNick.value.match(reNick)){
            resultNick.innerText='닉네임 형식이 맞지 않습니다.';
            resultNick.style.color='red';
            isNickOk=false;

        }else {
            resultNick.innerText='';
            isNickOk = true;

            if (btnNickChange&&isNickOk) {
                btnNickChange.addEventListener('click', async function (e) {

                    const confirmMessage = '닉네임을 변경하시겠습니까?';
                    const isConfirmed = confirm(confirmMessage);

                    const uid = document.querySelector('input[name=uid]').value;
                    if (isConfirmed) {
                        const jsonData={
                            "uid":uid,
                            "nick":inputNick.value,
                        };

                        console.log("아이디"+uid);
                        console.log("닉네임"+inputNick.value);

                        await fetch('/lotteon/my/formMyinfoNickChange',{
                            method:'POST',
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






}

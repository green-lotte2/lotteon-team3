window.onload = function() {

    let isPassOk=false;

    const rePass = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;

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

    const btnComplete = document.querySelector('.btnComplete');
    const inputPass1 = document.querySelector('input[name=pass1]');
    const inputPass2 = document.querySelector('input[name=pass2]');
    const resultPass = document.getElementById('result_pass');

    inputPass2.addEventListener('focusout',()=>{

        if (inputPass1.value === inputPass2.value) {
            if (!inputPass1.value.match(rePass)) {
                resultPass.innerText = '비밀번호 형식에 맞지 않습니다.';
                resultPass.style.color = 'red';
                isPassOk = false;
            } else {
                resultPass.innerText = '사용 가능한 비밀번호 입니다.';
                resultPass.style.color = 'green';
                isPassOk = true;
            }
        } else {
            resultPass.innerText = '비밀번호가 일치하지 않습니다.';
            resultPass.style.color = 'red';
            isPassOk = false;
        }
        console.log("비밀번호"+isPassOk);
    });

    if(btnComplete) {
        btnComplete.addEventListener('click', async function (e) {
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
                        alert('비밀번호가 변경되었습니다.');
                        document.getElementById('popPassChange').closest('.popup').classList.remove('on');
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

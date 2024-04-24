// 이메일 유효성 검사
window.onload = function() {

    let isEmailOk = false;
    let isEmailCodeOk = false;

    const inputEmail = document.getElementById('inputEmail'); //이메일 입력
    const btn_email = document.getElementById('btn_email'); // 이메일 인증번호받기버튼
    const resultEmail = document.getElementById('result_email');//이메일 중복결과 출력
    const inputName=document.getElementById('inputName');

    btn_email.onclick = function () {

        const type = this.dataset.type;

        console.log("type : " + type);

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        // 이메일 중복 체크 및 이메일 전송
        setTimeout(async () => {

            console.log('inputEmail value : ' + inputEmail.value);
            console.log('inputName value : '+inputName.value)

            const data = await fetchGet(`/lotteon/member/check/${type}/${inputName.value}/${inputEmail.value}`);
            console.log('data : ' + data.result);

            if (data.result > 0) {
                resultEmail.innerText = '인증코드 전송이 완료되었습니다. 이메일을 확인해주세요.';
                resultEmail.style.color = 'black';
                isEmailOk = true;
                // 이메일 인증번호 입력칸 활성화
                inputEmailCode.removeAttribute('disabled');
                inputEmail.setAttribute('readonly', 'true');
                // 이메일 인증번호를 입력할 수 있는 input에 포커스 설정
                inputEmailCode.focus();
            } else {
                resultEmail.innerText = '등록되지않은 회원의 정보입니다.';
                resultEmail.style.color = 'red';
                isEmailOk = false;
            }

        }, 1000);

    }
// 이메일 인증코드 확인
    const btnCheckEmailCode = document.getElementById('btnCheckEmailCode'); //인증번호 확인 버튼
    const inputEmailCode = document.getElementById('inputEmailCode'); //인증번호 입력
    const resultEmailCode = document.getElementById('resultEmailCode'); //일치여부

    btnCheckEmailCode.onclick = async function () {

        async function fetchGet(url) {

            console.log("fetchData1...1");

            try {
                console.log("fetchData1...2");
                const response = await fetch(url);
                console.log("here1");

                if (!response.ok) {
                    console.log("here2");
                    throw new Error('response not ok');
                }

                const data = await response.json();
                console.log("data1 : " + data);
                return data;
            } catch (err) {
                console.log(err)
            }
        }

        const data = await fetchGet(`/lotteon/member/email/${inputEmailCode.value}`);

        if (!data.result) {
            resultEmailCode.innerText = '인증코드가 일치하지 않습니다.';
            resultEmailCode.style.color = 'red';
            isEmailCodeOk = false;
        } else {
            resultEmailCode.innerText = '이메일이 인증되었습니다.';
            resultEmailCode.style.color = 'green';
            isEmailCodeOk = true;
        }
    }


    const btnAreaNext = document.querySelector('.btnAreaNext');
    btnAreaNext.addEventListener('click', function(event) {
        event.preventDefault();
        if (isEmailOk && isEmailCodeOk)
            document.getElementById('formFindId').submit();
        else
            alert('이메일 인증이 완료되어야 합니다.');
    });
}
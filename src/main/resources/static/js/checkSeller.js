// 판매자회원 유효성 검사 및 중복검사

//유효성 검사에 사용할 상태변수
let isUidOk = false;
let isPassOk = false;
let isNameOk = false;
let isNickOk  = false;
let isEmailOk = false;
let isHpOk = false;
let isEmailCodeOk = false;
let isCompanyOk   = false;
let isBizRegNumOk = false;
let isComRegNumOk = false;
let isTelOk       = false;
let isFaxOk       = false;

// 유효성 검사에 사용할 정규표현식
const reUid = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName = /^[가-힣]{2,10}$/
const reNick  = /^[a-zA-Zㄱ-힣0-9][a-zA-Zㄱ-힣0-9]*$/;
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reHp = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;
const reCompany = /^\(주\)+[a-zA-Z가-힣0-9]{1,12}$/;
const reBizRegNum = /^[0-9]{3}-[0-9]{2}-[0-9]{5}$/;
const reComRegNum = /^[0-9]{4}-[가-힣]{2,6}-[0-9]{4,5}$/;
const reTel       = /^(0[2-8][0-5]?)-?([1-9]{1}[0-9]{2,3})-?([0-9]{4})$/;
const reFax       = /^\d{2,3}-\d{3,4}-\d{4}$/;

window.onload = function () {

    // 아이디 유효성 검사
    const btnCheckUid = document.getElementById('btnCheckUid');
    const resultUid = document.getElementById('result_uid');

    btnCheckUid.onclick = function () {

        const type = this.dataset.type;
        const input = document.registerForm[type];

        // 정규식 검사
        if (!input.value.match(reUid)) {

            resultUid.innerText = '아이디 형식이 맞지 않습니다.';
            resultUid.style.color='red';
            isUidOk = false;
            return;
        }

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

        setTimeout(async () => {

                console.log('value : ' + input.value);
                console.log('type:' + type);

                const data = await fetchGet(`/lotteon/member/check/${type}/${input.value}`);

                if (data.result > 0) { // 아이디가 중복된경우

                    resultUid.innerText = '이미 사용중인 아이디 입니다.';
                    resultUid.style.color='red';
                    isUidOk = false;

                } else {// 아이디가 중복되지않은 경우(사용가능한 경우)

                    resultUid.innerText = '사용 가능한 아이디 입니다.';
                    resultUid.style.color='green';
                    isUidOk = true;
                    console.log("isUidOk:" + isUidOk);
                }
            }, 1000
        );
    }

    // 비밀번호 유효성 검사
    const resultPass = document.getElementById('result_pass');

    document.registerForm.pass2.addEventListener('focusout', () => {

        const inputPass1 = document.registerForm.pass;
        const inputPass2 = document.registerForm.pass2;

        console.log("비밀번호 입력 : " + inputPass1);
        console.log("비밀번호 확인 : " + inputPass2);


        if (inputPass1.value === inputPass2.value) {

            if (!inputPass1.value.match(rePass)) {
                resultPass.innerText = '비밀번호 형식에 맞지 않습니다.';
                resultPass.style.color='red';
                isPassOk = false;
            } else {
                resultPass.innerText = '사용 가능한 비밀번호 입니다.';
                resultPass.style.color='green';
                isPassOk = true;
            }
        } else {
            resultPass.innerText = '비밀번호가 일치하지 않습니다.';
            resultPass.style.color='red';
            isPassOk = false;
        }
    });
    
    //판매자 정보 입력

    // 회사명 유효성 검사
    const btnCheckCompany=document.getElementById('btnCheckCompany');
    const resultCompany=document.getElementById('result_company');

    btnCheckCompany.onclick=function (){

        const type=this.dataset.type;
        const input=document.registerForm[type];

        if(!input.value.match(reCompany)){

            resultCompany.innerText='유효한 회사명 형식이 아닙니다. (주)포함 입력하여야 합니다.';
            resultCompany.style.color='red';
            isCompanyOk=false;
            return;
        }

        async function fetchGet(url){
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
        setTimeout(async () => {

                console.log('value : ' + input.value);
                console.log('type:' + type);

                const data = await fetchGet(`/lotteon/member/check/${type}/${input.value}`);

                if (data.result > 0) {

                    resultCompany.innerText = '이미 가입한 회사입니다.';
                    resultCompany.style.color='red';
                    isCompanyOk = false;

                } else {

                    resultCompany.innerText = '해당 회사로 가입 가능합니다.';
                    resultCompany.style.color='green';
                    isCompanyOk = true;
                }
            }, 1000
        );

    }

    // // 대표자 유효성 검사
    // const resultCeo = document.getElementById('result_ceo');
    //
    // document.registerForm.ceo.addEventListener('focusout', () => {
    //     const value = document.registerForm.ceo.value;
    //
    //     if (!value.match(reName)) {
    //         resultCeo.innerText = '이름 형식이 맞지 않습니다.';
    //         resultCeo.style.color='red';
    //         isNameOk = false;
    //     } else {
    //         resultCeo.innerText = '';
    //         isNameOk = true;
    //     }
    // });

    //사업자 등록번호 유효성 검사
    const btnCheckBizRegNum=document.getElementById('btnCheckBizRegNum');
    const resultBizRegNum=document.getElementById('result_bizRegNum');

    btnCheckBizRegNum.onclick=function (){

        const type=this.dataset.type;
        const input=document.registerForm[type];

        if(!input.value.match(reBizRegNum)){
            resultBizRegNum.innerText='유효한 사업자등록번호가 아닙니다.';
            resultBizRegNum.style.color='red';
            isBizRegNumOk=false;
            return;
        }
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

        setTimeout(async () => {

                console.log('value : ' + input.value);
                console.log('type:' + type);

                const data = await fetchGet(`/lotteon/member/check/${type}/${input.value}`);

                if (data.result > 0) {

                    resultBizRegNum.innerText = '이미 존재하는 사업자등록번호입니다.';
                    resultBizRegNum.style.color='red';
                    isBizRegNumOk = false;

                } else {

                    resultBizRegNum.innerText = '해당 사업자등록번호로 가입 가능합니다.';
                    resultBizRegNum.style.color='green';
                    isBizRegNumOk = true;
                }
            }, 1000
        );
    }
    
    //통신판매신고번호 유효성 검사
    const btnCheckComRegNum=document.getElementById('btnCheckComRegNum');
    const resultComRegNum=document.getElementById('result_comRegNum');

    btnCheckComRegNum.onclick=function (){
        const type=this.dataset.type;
        const input=document.registerForm[type];

        if(!input.value.match(reComRegNum)){
            resultComRegNum.innerText='유효한 통신판매신고번호가 아닙니다.';
            resultComRegNum.style.color='red';
            isComRegNumOk=false;
            return;
        }
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

        setTimeout(async () => {

                console.log('value : ' + input.value);
                console.log('type:' + type);

                const data = await fetchGet(`/lotteon/member/check/${type}/${input.value}`);

                if (data.result > 0) {

                    resultComRegNum.innerText = '이미 존재하는 통신판매신고번호입니다.';
                    resultComRegNum.style.color='red';
                    isComRegNumOk = false;

                } else {

                    resultComRegNum.innerText = '해당 통신판매신고번호로 가입 가능합니다.';
                    resultComRegNum.style.color='green';
                    isComRegNumOk = true;
                }
            }, 1000
        );

    }
    // 회사전화번호 유효성 검사
    const resultTel = document.getElementById('result_tel');

    document.registerForm.tel.addEventListener('focusout', () => {
        const type = document.registerForm.tel.dataset.type;
        const input = document.registerForm[type];


        // 정규식 검사
        if (!input.value.match(reTel)) {
            resultTel.innerText = '유효한 전화번호 양식이 아닙니다.';
            resultTel.style.color='red';
            isTelOk = false;
            return;
        }

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

        setTimeout(async () => {
            const data = await fetchGet(`/lotteon/member/check/${type}/${input.value}`);

            if (data.result > 0) {
                resultTel.innerText = '이미 존재하는 전화번호입니다.';
                resultTel.style.color='red';
                isTelOk = false;
            } else {
                resultTel.innerText = '해당 전화번호로 가입 가능합니다.';
                resultTel.style.color='green';
                isTelOk = true;
            }
        }, 1000);
    });

    //팩스 유효성 검사
    const resultFax = document.getElementById('result_fax');

    document.registerForm.fax.addEventListener('focusout', () => {
        const type = document.registerForm.fax.dataset.type;
        const input = document.registerForm[type];


        // 정규식 검사
        if (!input.value.match(reFax)) {
            resultFax.innerText = '유효한 팩스번호 양식이 아닙니다.';
            resultFax.style.color='red';
            isFaxOk = false;
            return;
        }

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

        setTimeout(async () => {
            const data = await fetchGet(`/lotteon/member/check/${type}/${input.value}`);

            if (data.result > 0) {
                resultFax.innerText = '이미 존재하는 팩스번호입니다.';
                resultFax.style.color='red';
                isFaxOk = false;
            } else {
                resultFax.innerText = '해당 팩스번호로 가입 가능합니다.';
                resultFax.style.color='green';
                isFaxOk = true;
            }
        }, 1000);
    });


    // 이메일 유효성 검사
    const divEmailCode = document.getElementById('divEmailCode');
    const inputEmail = document.getElementById('inputEmail');
    const btn_email = document.getElementById('btn_email');
    const resultEmail = document.getElementById('result_email');

    btn_email.onclick = function () {

        const type = this.dataset.type;

        console.log("type : " + type);

        // 유효성 검사
        if (!inputEmail.value.match(reEmail)) {

            resultEmail.innerText = '이메일 형식이 맞지 않습니다.';
            resultEmail.style.color='red';
            isEmailOk = false;
            return;
        }

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

            const data = await fetchGet(`/lotteon/member/check/${type}/${inputEmail.value}`);
            console.log('data : ' + data.result);

            if (data.result > 0) {
                resultEmail.innerText = '이미 사용중인 이메일 입니다.';
                resultEmail.style.color='red';
                isEmailOk = false;
            } else {
                resultEmail.innerText = '인증코드 전송이 완료되었습니다. 이메일을 확인해주세요.';
                resultEmail.style.color='black';
                // 이메일 인증번호 입력칸 활성화
                inputEmailCode.removeAttribute('disabled');
                // 이메일 인증번호를 입력할 수 있는 input에 포커스 설정
                inputEmailCode.focus();
            }

        }, 1000);

        isEmailOk = true;
    }
    // 이메일 인증코드 확인
    const btnCheckEmailCode = document.getElementById('btnCheckEmailCode');
    const inputEmailCode = document.getElementById('inputEmailCode');
    const resultEmailCode = document.getElementById('resultEmailCode');

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
            resultEmailCode.style.color='red';
            isEmailCodeOk = false;
        } else {
            resultEmailCode.innerText = '이메일이 인증되었습니다.';
            resultEmailCode.style.color='green';
            isEmailCodeOk = true;
        }
    }
    
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
                document.getElementById('inputZip').value = data.zonecode;
                document.getElementById("inputAddr1").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("inputAddr2").focus();
            }
        }).open();
    }

    findZip.onclick = function () {
        postcode();
    }
    
    // // 담당자 이름 유효성 검사
    // const resultManager = document.getElementById('result_manager');
    //
    // document.registerForm.manager.addEventListener('focusout', () => {
    //     const value = document.registerForm.manager.value;
    //
    //     if (!value.match(reName)) {
    //         resultManager.innerText = '이름 형식이 맞지 않습니다.';
    //         resultManager.style.color='red';
    //         isNameOk = false;
    //     } else {
    //         resultManager.innerText = '';
    //         isNameOk = true;
    //     }
    // });

    // 담당자 휴대폰 유효성 검사
    const resultManagerHp = document.getElementById('result_managerHp');

    document.registerForm.managerHp.addEventListener('focusout', () => {
        const value=document.registerForm.managerHp.value;

        // 정규식 검사
        if (!value.match(reHp)) {
            resultManagerHp.innerText = '휴대폰 형식이 맞지 않습니다.';
            resultManagerHp.style.color = 'red';
            isHpOk = false;
        }else {
            resultManagerHp.innerText = '';
            isHpOk = true;
            }
        });

    

    // 최종 유효성 검사 확인
    document.registerForm.onsubmit = function () {

        if (!isUidOk) {
            alert("아이디가 유효하지 않습니다.!");
            return false;
        }

        if (!isPassOk) {
            alert('비밀번호가 유효하지 않습니다.');
            return false;
        }

        if (!isCompanyOk) {
            alert('회사명이 유효하지 않습니다.');
            return false;
        }

        if(!isBizRegNumOk){
            alert('사업자 등록번호가 유효하지 않습니다.');
            return false;
        }
        if(!isComRegNumOk){
            alert('통신 판매업 신고번호가 유효하지 않습니다.');
            return false;
        }
        if(!isTelOk){
            alert('전화번호가 유효하지 않습니다.');
            return false;
        }

        if (!isEmailOk) {
            alert('이메일이 유효하지 않습니다.');
            return false;
        }

        if (!isEmailCodeOk) {
            alert('이메일 인증을 해주세요.');
            return false;
        }

        if (!isHpOk) {
            alert('휴대폰이 유효하지 않습니다.');
            return false;
        }

        if (document.getElementById('inputZip').value === '') {
            alert('주소를 입력해주세요');
            return false;
        }

        // 폼 전송
        return true;
    }

}

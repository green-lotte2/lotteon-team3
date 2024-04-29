window.onload = function () {
    //닉네임
    document.getElementById("btnChangeNick").addEventListener("click", function () {

        // 취소 버튼 생성
        const cancelButton = document.createElement("button");
        // 현재 닉네임을 가져옴
        const currentNick = document.getElementById("nick").innerText;

        // 닉네임을 수정할 input 요소 생성
        const inputNick = document.createElement("input");
        inputNick.value = currentNick;

        // 완료 버튼 생성
        const completeButton = document.createElement("button");
        completeButton.textContent = "완료";
        completeButton.addEventListener("click", function () {
            const newNick = inputNick.value;
            document.getElementById("nick").innerText = newNick;

            // input과 버튼 제거
            inputNick.remove();
            completeButton.remove();
            cancelButton.remove();
        });
        cancelButton.textContent = "취소";
        cancelButton.addEventListener("click", function () {
            // input과 버튼 제거
            inputNick.remove();
            completeButton.remove();
            cancelButton.remove();
        });

        // td 안에 요소 추가
        const tdElement = document.getElementById('newNick');
        tdElement.appendChild(inputNick);
        tdElement.appendChild(completeButton);
        tdElement.appendChild(cancelButton);

        // btnChangeNick 버튼 제거
        document.getElementById("btnChangeNick").remove();
        document.getElementById("nick").remove();
    });

    //이메일
    document.getElementById("btnChangeEmail").addEventListener("click", function () {

        // 취소 버튼 생성
        const cancelButton = document.createElement("button");
        // 현재 닉네임을 가져옴
        const currentEmail = document.getElementById("email").innerText;

        // 이메일을 수정할 input 요소 생성
        const inputEmail = document.createElement("input");
        inputEmail.value = currentEmail.substring(0, currentEmail.indexOf('@'));

        const atMark=document.createElement("span");
        atMark.textContent="@";

        const inputDomain = document.createElement("select");
        inputDomain.value = currentEmail.substring(currentEmail.indexOf('@') + 1);

        const domainOptions = ["naver.com", "daum.net", "gmail.com", "nate.com"];
        
        const checkEmail=document.createElement("input");
        checkEmail.placeholder="인증번호 입력"

        const checkEmailButton=document.createElement("button");
        checkEmailButton.textContent="인증번호 확인";

        const resultEmail=document.createElement("span");
        resultEmail.textContent=""; // 유효성 검사 결과여부

        domainOptions.forEach(domain => {
            const option = document.createElement("option");
            option.textContent = domain;
            inputDomain.appendChild(option);
        });


        // 완료 버튼 생성
        const completeButton = document.createElement("button");
        completeButton.textContent = "완료";
        completeButton.addEventListener("click", function () {
            const newEmail = inputDomain.value;
            document.getElementById("nick").innerText = newEmail;

            // input과 버튼 제거
            inputDomain.remove();
            completeButton.remove();
            cancelButton.remove();
        });
        cancelButton.textContent = "취소";
        cancelButton.addEventListener("click", function () {
            // input과 버튼 제거
            inputDomain.remove();
            completeButton.remove();
            cancelButton.remove();
        });

        // td 안에 요소 추가
        const tdElement = document.getElementById('newEmail');
        const resultElement = document.getElementById('resultEmail');
        tdElement.appendChild(inputEmail);
        tdElement.appendChild(atMark);
        tdElement.appendChild(inputDomain);

        tdElement.appendChild(completeButton);
        tdElement.appendChild(cancelButton);
        resultElement.appendChild(checkEmail);
        resultElement.appendChild(checkEmailButton);

        // btnChangeNick 버튼 제거
        document.getElementById("btnChangeEmail").remove();
        document.getElementById("email").remove();
    });


}
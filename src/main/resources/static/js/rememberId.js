document.addEventListener("DOMContentLoaded", function() {

    // 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
    const key = getCookie("key");
    const idInput = document.getElementById("inputUid");

    if (idInput) {
        idInput.value = key;
    }

    // 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
    const checkIdCheckbox = document.getElementById("checkId");
    if (checkIdCheckbox && idInput && idInput.value !== "") {
        checkIdCheckbox.checked = true; // ID 저장하기를 체크 상태로 두기.
    }

    if (checkIdCheckbox && idInput) {
        checkIdCheckbox.addEventListener("change", function() {
            // 체크박스에 변화가 있다면,
            if (checkIdCheckbox.checked) {
                // ID 저장하기 체크했을 때,
                setCookie("key", idInput.value, 7); // 7일 동안 쿠키 보관
            } else {
                // ID 저장하기 체크 해제 시,
                deleteCookie("key");
            }
        });

        // ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
        idInput.addEventListener("keyup", function() {
            // ID 입력 칸에 ID를 입력할 때,
            if (checkIdCheckbox.checked) {
                // ID 저장하기를 체크한 상태라면,
                setCookie("key", idInput.value, 7); // 7일 동안 쿠키 보관
            }
        });
    }
});

// 쿠키 저장하기
function setCookie(cookieName, value, exdays) {
    const exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    const cookieValue = encodeURIComponent(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
    document.cookie = cookieName + "=" + cookieValue;
}

// 쿠키 삭제
function deleteCookie(cookieName) {
    const expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toUTCString();
}

// 쿠키 가져오기
function getCookie(cookieName) {
    cookieName = cookieName + '=';
    const cookieData = document.cookie;
    let start = cookieData.indexOf(cookieName);
    let cookieValue = '';
    if (start != -1) { // 쿠키가 존재하면
        start += cookieName.length;
        let end = cookieData.indexOf(';', start);
        if (end == -1) // 쿠키 값의 마지막 위치 인덱스 번호 설정
            end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return decodeURIComponent(cookieValue);
}
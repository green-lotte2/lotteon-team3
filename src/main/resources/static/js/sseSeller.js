// 로그인 상태면
if (isAuthenticated) {
    console.log("sse.js isAuthenticated ");
    // 연결 최초 요청
    const eventSource = new EventSource("/lotteon/connect")
    eventSource.addEventListener("connect", function (event) {
        let message = event.data;
        console.log("message " + message);
        if (message !== "none") {
            Swal.fire({
                // toast:true,
                position: 'top',
                icon: 'success',
                title: `<div style='color:#413f3f;font-size:15px'>` + message + `<br>`+ '바로 이동하시겠습니까?'+ `</div>`,
                timer: 4000,
                showConfirmButton: true,// ok 버튼 노출 여부
                showDenyButton: true, // No 버튼 노출 여부
                confirmButtonText : "예", // 버튼 문구 수정
                denyButtonText : "아니오", // 버튼 문구 수정
            })
                .then( (result) => {
                    if (result.isConfirmed) {
                        location.href = "/lotteon/seller/cs/list?group=qna&cate=";
                    } else if (result.isDenied) {

                    }
                })

            eventSource.onerror = function(error) {
                console.error('EventSource failed:', error);
            };
        }
    })

    // 실시간 알람을 받으면
    eventSource.onmessage = function(event) {
        console.log("알람 옴 !!!");
        const message = event.data;
        // 받은 알림을 화면에 출력
        displayNotification(message);
    };

    function displayNotification(message) {
        console.log("message " + message);
        Swal.fire({
            // toast:true,
            position: 'top',
            icon: 'success',
            title: `<div style='color:#413f3f;font-size:15px'>` + message + `<br>`+ '바로 이동하시겠습니까?'+ `</div>`,
            timer: 4000,
            showConfirmButton: true,// ok 버튼 노출 여부
            showDenyButton: true, // No 버튼 노출 여부
            confirmButtonText : "예", // 버튼 문구 수정
            denyButtonText : "아니오", // 버튼 문구 수정
        })
            .then( (result) => {
                if (result.isConfirmed) {
                    location.href = "/lotteon/seller/cs/list?group=qna&cate=";
                } else if (result.isDenied) {

                }
            })

        eventSource.onerror = function(error) {
            console.error('EventSource failed:', error);
        };
    }

}


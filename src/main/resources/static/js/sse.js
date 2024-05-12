// 로그인 상태면
if (isAuthenticated) {
    console.log("sse.js isAuthenticated ");
    // 연결 최초 요청
    const eventSource = new EventSource("/lotteon/connect")
    eventSource.addEventListener("connect", function (event) {
        let message = event.data;
        console.log("message " + message);
            eventSource.onerror = function(error) {
                console.error('EventSource failed:', error);
            };
    })
}


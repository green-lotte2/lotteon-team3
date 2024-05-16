var contextPath = /*[[@{/}]]*/ ''; // 이 부분은 프로젝트의 context path를 동적으로 가져오는 Thymeleaf 문법입니다.

// 1주일, 14일, 1개월 버튼 클릭 시 이벤트
function changeDate(element, str) {
    // 모든 요소에서 'on' 클래스 제거
    var datePickElements = document.querySelectorAll('.datePick');
    datePickElements.forEach(function(item) {
        item.classList.remove('on');
    });
    // 클릭한 요소에 'on' 클래스 추가
    element.classList.add('on');

    var today = new Date(); // 현재 날짜를 가져옵니다
    // .
    var oneWeekAgo = new Date(today);
    oneWeekAgo.setDate(today.getDate() - 7); // 일주일 전의 날짜를 계산합니다.

    var fifteenDaysAgo = new Date(today);
    fifteenDaysAgo.setDate(today.getDate() - 14); // 14일 전의 날짜를 계산합니다.

    var oneMonthAgo = new Date(today);
    oneMonthAgo.setMonth(today.getMonth() - 1); // 한 달 전의 날짜를 계산합니다.

    if (str === 'week') {
        setDatePickerValue(oneWeekAgo, 'begin');
    } else if (str === '14days') {
        setDatePickerValue(fifteenDaysAgo, 'begin');
    } else if (str === 'month') {
        setDatePickerValue(oneMonthAgo, 'begin');
    }
    setDatePickerValue(today, 'end');
}

// 선택값에 따라 일자를 설정해준다.
function setDatePickerValue(date, str) {
    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 1을 더하고 2자리로 만듭니다.
    var day = date.getDate().toString().padStart(2, '0'); // 일자를 2자리로 만듭니다.

    var dateString = `${year}-${month}-${day}`;
    if (str === 'end') {
        document.querySelector('input[name=end]').value = dateString;
    } else {
        document.querySelector('input[name=begin]').value = dateString;
    }
}

// 테이블 리로드(조회하기 눌렸을때 시작일, 종료일, 페이지 넘겨줌)
function tableReload(pg) {
    let beginDate = document.querySelector('input[name=begin]').value;
    let endDate = document.querySelector('input[name=end]').value;
    const jsonData = {
        "begin": beginDate,
        "end": endDate,
        "pg": pg
    };

    // 페이지 로드 시 URL 변경
    const url = new URL(window.location.href);
    url.searchParams.set('pg', pg); // pg 부분을 변경
    window.history.pushState({ path: url.href }, '', url.href);

    // 아직 모르겠음
    fetch(`/lotteon/my/pointList?begin=${beginDate}&end=${endDate}&pg=${pg}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            // 페이지 업데이트
            updatePagination(data);

            // 리스트 업데이트
            updatePointList(data);
        })
        .catch(error => console.error('Error:', error));
}

// 페이지네이션 업데이트 함수
function updatePagination(data) {
    const page = document.querySelector('.page');
    page.innerHTML = '';

    if (data.prev) {
        page.innerHTML += `<span class="prev">
                                <a href="#" onclick="tableReload(${data.start - 1}); return false;">이전</a>
                            </span>`;
    }

    for (let i = data.start; i <= data.end; i++) {
        page.innerHTML += `<span>
                                <a href="#" class="num ${data.pg == i ? 'on' : ''}" onclick="tableReload(${i}); return false;">${i}</a>
                            </span>`;
    }

    if (data.next) {
        page.innerHTML += `<span class="next">
                                <a href="#" onclick="tableReload(${data.end + 1}); return false;">다음</a>
                            </span>`;
    }

    // 조회된 데이터가 없는 경우 메시지를 추가하고, 데이터가 있는 경우 메시지를 제거합니다.
    const pointTable = document.querySelector('.pointList');
    const tableHeader = `
                        <tr>
                            <th>적립날짜</th>
                            <th>구분</th>
                            <th>주문번호</th>
                            <th>적립금액</th>
                            <th>비고</th>
                            <th>유효기간</th>
                        </tr>`;

    if (data.dtoList.length === 0) {
        pointTable.innerHTML = `<tr>
                                    <td colspan="6" style="text-align: center;">조회된 포인트 내역이 없습니다.</td>
                                </tr>`;
    } else {
        pointTable.innerHTML = tableHeader; // 조회된 데이터가 있을 경우 테이블 헤더를 추가합니다.
    }
}



// 리스트 업데이트 함수
function updatePointList(data) {
    const pointList = document.querySelector('.pointList');
    // 첫 번째 <tr>를 제외한 모든 <tr> 초기화
    while (pointList.rows.length > 1) {
        pointList.deleteRow(1);
    }
    const currentDate = new Date();

    data.dtoList.forEach(dto => {
// 날짜 문자열을 ISO 8601 형식으로 변환
        let dateString = `${dto.pointDate[0]}-${dto.pointDate[1].toString().padStart(2, '0')}-${dto.pointDate[2].toString().padStart(2, '0')}T${dto.pointDate[3]}:${dto.pointDate[4]}:${dto.pointDate[5]}`;

        console.log("계산전 만료 날짜"+dateString);

// 새로운 Date 객체 생성
        let dateP = new Date(dateString);

        console.log("계산전 만료 날짜"+dateP);
        console.log("현재날짜"+currentDate);


        const timeDiff = currentDate.getTime() - dateP.getTime();
        const diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

        console.log('현재날짜와 소멸날짜의 차이:', diffDays);

        // 새로운 <tr> 요소를 생성하여 테이블에 추가
        let newRow = pointList.insertRow(-1);
        newRow.insertCell(0).textContent = `${dto.currentDate[0]}-${dto.currentDate[1].toString().padStart(2, '0')}-${dto.currentDate[2].toString().padStart(2, '0')}`;


        if(diffDays>1||diffDays===0){
            newRow.insertCell(1).textContent = '소멸';
            newRow.cells[1].style.color = '#999'
        }else if(diffDays<0){
            newRow.insertCell(1).textContent = '적립';
        }else if(diffDays===1){
            newRow.insertCell(1).textContent = '소멸예정';
            newRow.cells[1].style.color = '#ef2a23'
        }

        newRow.insertCell(2).textContent = dto.ordNo;
        newRow.insertCell(3).textContent = dto.point;
        newRow.insertCell(4).textContent = dto.usecase;
        newRow.insertCell(5).textContent = `${dto.pointDate[0]}-${dto.pointDate[1].toString().padStart(2, '0')}-${dto.pointDate[2].toString().padStart(2, '0')}`;
    });
}


// xx월 클릭 시 이벤트
function changeMonth(element, month, year) {
    // 모든 요소에서 'on' 클래스 제거
    var datePickElements = document.querySelectorAll('.datePick');
    datePickElements.forEach(function(item) {
        item.classList.remove('on');
    });
    // 클릭한 요소에 'on' 클래스 추가
    element.classList.add('on');

    const dates = getFirstAndLastDate(year, month);
    setDatePickerValue(dates.firstDate, 'begin');
    setDatePickerValue(dates.lastDate, 'end');
}

// 페이지 시작
document.addEventListener('DOMContentLoaded', function() {
    // 페이지 시작 시 오늘 이후의 날짜를 선택할 수 없도록 설정
    const beginInput = document.querySelector('input[name=begin]');
    const endInput = document.querySelector('input[name=end]');
    const todayString = new Date().toISOString().split('T')[0]; // 현재 날짜를 YYYY-MM-DD 형식으로 가져옵니다.
    beginInput.setAttribute('max', todayString);
    endInput.setAttribute('max', todayString);

    // 오늘 날짜를 기준으로 5개월 전까지의 달을 구한다.
    const date5 = document.querySelector('.date_5ea');
    const today = new Date();

    date5.innerHTML = "";

    for (let i = 0; i < 5; i++) {
        let currentMonth = today.getMonth() - i + 1;
        let currentYear = today.getFullYear();

        if (currentMonth < 0) {
            currentMonth += 12;
            currentYear--;
        }

        date5.innerHTML += `<li><a href="#" class="datePick" onclick="changeMonth(this, ${currentMonth}, ${currentYear}); return false;"><em>${currentMonth}</em>월</a></li>`;
    }

    // 조회하기 버튼 클릭 시 리스트 조회 기능
    const btnConfirm = document.querySelector('.btnConfirm');
    btnConfirm.addEventListener('click', function() {
        tableReload(1);
    });

    // end가 begin보다 작을 경우 begin을 end와 동일한 값으로 변경
    document.querySelector('input[name=end]').addEventListener('change', function() {
        const begin = document.querySelector('input[name=begin]').value;
        const end = document.querySelector('input[name=end]').value;

        if (begin > end) {
            document.querySelector('input[name=begin]').value = end;
        }
    });

    // begin이 end보다 클 경우 end를 begin과 동일한 값으로 변경
    document.querySelector('input[name=begin]').addEventListener('change', function() {
        const begin = document.querySelector('input[name=begin]').value;
        const end = document.querySelector('input[name=end]').value;

        if (begin > end) {
            document.querySelector('input[name=end]').value = begin;
        }
    });
});

// 년도와 월을 넘기면 해당 달의 시작일과 마지막일을 리턴한다.
function getFirstAndLastDate(year, month) {
    // 월을 0부터 11까지의 숫자로 변환 (0이 1월, 11이 12월을 의미)
    month = month - 1;

    // 다음 달의 첫 날을 계산
    var firstDay = new Date(year, month, 1);

    // 다음 달의 첫 날에서 하루를 빼서 이번 달의 마지막 날을 계산
    var lastDay = new Date(year, month + 1, 0);

    return {
        firstDate: firstDay,
        lastDate: lastDay
    };
}
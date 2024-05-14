document.addEventListener('DOMContentLoaded', async function() {
    fetch('/lotteon/my/myInfo')

        .then(response => {
            console.log("fetch");
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);

            document.getElementById('orderCount').textContent = data.orderCount.toLocaleString();
            document.getElementById('couponCount').textContent = data.couponCount.toLocaleString();
            document.getElementById('myPoint').textContent = data.myPoint.toLocaleString();
            document.getElementById('qnaCount').textContent = data.qnaCount.toLocaleString();
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });

});
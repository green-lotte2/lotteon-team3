document.addEventListener("DOMContentLoaded", function() {
    const btnPassCheck = document.querySelector('.btnPassCheck');
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
                .then(response=>response.json())
                .then(data=>{
                    console.log(data);
                    if(data){
                        location.href = `/lotteon/my/info?uid=${uid}`;
                    }else{
                        alert("비밀번호가 올바르지 않습니다.");
                    }

                })
                .catch((err) => {
                    console.log(err);
                    alert("비밀번호가 올바르지 않습니다.");
                });
    });
});

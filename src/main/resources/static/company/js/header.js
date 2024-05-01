window.addEventListener('scroll', function() {
    const top = window.scrollY || document.documentElement.scrollTop;

    if (top > 10) {
        document.querySelector('#wrapper > header').style.background = 'white';
        const headerLinks = document.querySelectorAll('#wrapper > header a');
        headerLinks.forEach(function(link) {
            link.style.color = '#222';
        });
        document.querySelector('#wrapper > header img').src = './images/header_logo.png';
    } else {
        document.querySelector('#wrapper > header').style.background = 'transparent';
        const headerLinks = document.querySelectorAll('#wrapper > header a');
        headerLinks.forEach(function(link) {
            link.style.color = '#fff';
        });
        document.querySelector('#wrapper > header img').src = './images/header_logo.png';
    }
});

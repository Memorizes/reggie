function loginApi(data) {
    return $axios({
        'url': '/user/login',
        'method': 'post',
        data
    })
}

function sendMsgApi(data) {
    return $axios({
        'url': '/user/sendMsg',
        'method': 'post',
        'timeout': 30 * 1000,
        data
    })
}

function loginoutApi() {
    return $axios({
        'url': '/user/loginout',
        'method': 'post',
    })
}
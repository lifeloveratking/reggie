function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
       data
    })
  }

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}
// src/main/resources/front/api/login.js
// 添加发送短信的接口
function sendMsgApi(data) {
    return $axios({
        'url': '/user/sendMsg',
        'method': 'post',
        data
    })
}

  
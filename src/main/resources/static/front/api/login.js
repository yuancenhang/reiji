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

//自己写的获取验证码函数
function getMa(data) {
    return $axios({
        'url':"/user/getCode",
        'method':"get",
        params:{...data}
    })
}

  
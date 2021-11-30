import http from "@/api/http.js"

const sendEmailForSignup = async (email) => {
  const res = await http.get(`/auth/cert/signup/${email}`)
  return res.data
}

const sendEmailForFindPW = async (email) => {
  const res = await http.get(`/auth/cert/password/${email}`)
  return res.data
}

const confirmEmailAuthCode = async ({ requestData, id }) => {
  const res = await http.post(`/auth/cert/authcode/${id}`, requestData)
  return res.data
}

const checkNickname = async (nickname) => {
  const res = await http.get(`/auth/check/nickname/${nickname}`)
  return res.data
}

const signup = async (formData) => {
  await http.post(`/auth/signup`, formData)
}

const login = async (formData) => {
  const res = await http.post(`/auth/login`, formData)
  return res.data
}

const reissue = async (tokenData) => {
  console.log(tokenData)
  const res = await http.post(`/auth/reissue`, tokenData)
  return res.data
}

const findPassword = async ({ email, id, password }) => {
  await http.post(`auth/findpassword`, { email, id, password })
}

export default {
  sendEmailForSignup,
  sendEmailForFindPW,
  confirmEmailAuthCode,
  checkNickname,
  signup,
  login,
  reissue,
  findPassword,
}

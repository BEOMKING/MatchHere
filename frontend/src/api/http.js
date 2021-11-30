import router from "@/router"
import store from "@/store"
import axios from "axios"

const http = axios.create({
  baseURL: "http://127.0.0.1:8080/api",
})

http.interceptors.request.use(
  async (config) => {
    // console.log(config)
    let tokenData = store.getters["auth/getToken"]
    if (!tokenData.accessToken || config.url.includes("reissue")) {
      return config
    }
    // accessToken이 유효한지 확인한다.
    if (tokenData.accessTokenExpiresIn < Date.now()) {
      // accessToken 유효기간 만료
      try {
        // token 재발급
        await store.dispatch("auth/reissue", tokenData)
        console.log("try")
        tokenData = store.getters["autn/getToken"]
      } catch (error) {
        // refreshToken 유효기간 만료
        router.push({ name: "Login" })
        console.log("RESET_TOKEN")
        store.commit("auth/RESET_TOKEN")
        return
      }
    }
    config.headers.Authorization = `Bearer ${tokenData.accessToken}`
    return config
  },
  (error) => {
    console.log(error)
  }
)

export default http

import { createApp } from "vue"
import App from "./App.vue"
import router from "./router"
import store from "./store"
import "./assets/tailwind.css"

if (localStorage.getItem("signupFormData")) {
  store.commit(
    "auth/SET_SIGNUP_FORMDATA",
    JSON.parse(localStorage.getItem("signupFormData"))
  )
}
if (localStorage.getItem("signupStep")) {
  store.commit("auth/SET_SIGNUP_STEP", localStorage.getItem("signupStep"))
}

const createAndMountVue = async () => {
  if (localStorage.getItem("token")) {
    try {
      await store.dispatch(
        "auth/reissue",
        JSON.parse(localStorage.getItem("token"))
      )
    } catch (error) {
      console.log(error)
      store.commit("auth/RESET_TOKEN")
    }
    try {
      await store.dispatch("member/getMe")
    } catch (error) {
      console.log(error)
    }
  }
  createApp(App).use(store).use(router).mount("#app")
}

createAndMountVue()

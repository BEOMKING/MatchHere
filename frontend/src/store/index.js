import { createStore } from "vuex"

import auth from "@/store/modules/auth"
import project from "@/store/modules/project"
import member from "@/store/modules/member"
import file from "@/store/modules/file"
import chat from "@/store/modules/chat"
import study from "@/store/modules/study"

export default createStore({
  state: {
    alertMessages: {},
    modalOpen: false,
  },
  mutations: {
    ADD_MESSAGE(state, message) {
      const key = Date.now()
      state.alertMessages[key] = message
      setTimeout(() => {
        delete state.alertMessages[key]
      }, 3000)
    },
    SET_MODAL_OPEN(state, modalState) {
      state.modalOpen = modalState
    },
  },
  actions: {},
  modules: { auth, project, member, file, chat, study },
})

import AuthAPI from "@/api/auth"

export default {
  namespaced: true,
  state: {
    emailCertId: 0,
    emailCertRequestDto: {
      email: "",
      authCode: "",
    },
    signupFormData: {
      id: 0,
      password: "",
      nickname: "",
      name: "",
      city: "",
      position: "",
      dpositionList: [],
      techList: [],
    },
    findPasswordFormData: {
      id: 0,
      email: "",
      password: "",
    },
    token: {
      grantType: "",
      accessToken: "",
      accessTokenExpiresIn: 0,
      refreshToken: "",
    },
    findPWRequestDto: {
      id: 0,
      email: "",
      password: "",
    },
  },
  mutations: {
    SET_EMAIL_CERT_DTO(state, payload) {
      state.emailCertRequestDto = { ...state.emailCertRequestDto, ...payload }
    },
    SET_EMAIL_CERT_ID(state, id) {
      state.emailCertId = id
    },
    SET_SIGNUP_FORMDATA(state, formData) {
      state.signupFormData = { ...state.signupFormData, ...formData }
    },
    RESET_SIGNUP_FORMDATA(state) {
      state.signupFormData = {
        id: 0,
        password: "",
        nickname: "",
        name: "",
        city: "",
        position: "",
        dpositionList: [],
        techList: [],
      }
    },
    SET_FIND_PW_FORM_DATA(state, formData) {
      state.findPasswordFormData = {
        ...state.findPasswordFormData,
        ...formData,
      }
    },
    SET_TOKEN(state, tokenData) {
      state.token = tokenData
      localStorage.setItem("token", JSON.stringify(tokenData))
    },
    RESET_TOKEN(state) {
      console.log("RESET_TOKEN")
      state.token = {
        grantType: "",
        accessToken: "",
        accessTokenExpiresIn: 0,
        refreshToken: "",
      }
      localStorage.removeItem("token")
    },
  },
  actions: {
    async sendEmailForSignup({ commit }, email) {
      try {
        const id = await AuthAPI.sendEmailForSignup(email)
        commit(
          "ADD_MESSAGE",
          { text: "인증 메일을 전송했습니다" },
          { root: true }
        )
        commit("SET_EMAIL_CERT_DTO", { email })
        commit("SET_EMAIL_CERT_ID", id)
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          { text: "이미 가입된 이메일입니다", type: "error" },
          { root: true }
        )
        throw Error()
      }
    },
    async sendEmailForFindPW({ commit }, email) {
      try {
        const id = await AuthAPI.sendEmailForFindPW(email)
        commit(
          "ADD_MESSAGE",
          { text: "인증 메일을 전송했습니다" },
          { root: true }
        )
        commit("SET_EMAIL_CERT_DTO", { email })
        commit("SET_EMAIL_CERT_ID", id)
        commit("SET_FIND_PW_FORM_DATA", { email })
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          { text: "가입하지 않은 이메일입니다", type: "error" },
          { root: true }
        )
        throw Error(error)
      }
    },
    async confirmEmailAuthCode({ getters, commit }, authCode) {
      try {
        commit("SET_EMAIL_CERT_DTO", { authCode })
        const id = await AuthAPI.confirmEmailAuthCode(
          getters["getEmailAuthData"]
        )
        commit("ADD_MESSAGE", { text: "이메일 인증 성공 😎" }, { root: true })
        commit("SET_SIGNUP_FORMDATA", { id })
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          { text: "올바르지 않은 인증코드입니다", type: "error" },
          { root: true }
        )
        throw Error()
      }
    },
    async confirmEmailAuthCodeForFindPW({ getters, commit }, authCode) {
      try {
        commit("SET_EMAIL_CERT_DTO", { authCode })
        const id = await AuthAPI.confirmEmailAuthCode(
          getters["getEmailAuthData"]
        )
        commit("ADD_MESSAGE", { text: "이메일 인증 성공 😎" }, { root: true })
        commit("SET_FIND_PW_FORM_DATA", { id })
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          { text: "올바르지 않은 인증코드입니다", type: "error" },
          { root: true }
        )
        throw Error()
      }
    },
    async submitStepOne({ commit }, formData) {
      try {
        const isDuplicated = await AuthAPI.checkNickname(formData.nickname)
        if (!isDuplicated) {
          commit("SET_SIGNUP_FORMDATA", formData)
        } else {
          commit(
            "ADD_MESSAGE",
            { text: "이미 존재하는 닉네임입니다 🥲", type: "error" },
            { root: true }
          )
        }
        return isDuplicated
      } catch (error) {
        throw new Error(error)
      }
    },
    async signup({ state, commit }, formData) {
      commit("SET_SIGNUP_FORMDATA", formData)
      try {
        await AuthAPI.signup(state.signupFormData)
        commit("RESET_SIGNUP_FORMDATA")
        commit("ADD_MESSAGE", { text: "회원가입 성공 😎" }, { root: true })
      } catch (error) {
        throw new Error(error.message)
      }
    },
    async login({ commit }, formData) {
      try {
        const { email, id, name, nickname, tokenInfo } = await AuthAPI.login(
          formData
        )
        commit("SET_TOKEN", tokenInfo)
        commit("member/SET_USER", { email, id, name, nickname }, { root: true })
        commit("ADD_MESSAGE", { text: "안녕하세요 👍" }, { root: true })
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          { text: "이메일 또는 비밀번호를 확인하세요", type: "error" },
          {
            root: true,
          }
        )
        throw new Error()
      }
    },
    async logout({ commit }) {
      commit("RESET_TOKEN")
      commit(
        "ADD_MESSAGE",
        {
          text: "로그아웃 성공",
          type: "success",
        },
        { root: true }
      )
    },
    async reissue({ commit }, tokenData) {
      try {
        const { accessToken, refreshToken } = tokenData
        const newTokenData = await AuthAPI.reissue({
          accessToken,
          refreshToken,
        })
        commit("SET_TOKEN", newTokenData)
      } catch (error) {
        console.log(error)
        throw new Error(error)
      }
    },
    async findPassword({ commit, getters }, { password }) {
      commit("SET_FIND_PW_FORM_DATA", { password })
      try {
        await AuthAPI.findPassword(getters["getFindPWFormData"])
        commit(
          "ADD_MESSAGE",
          {
            text: "비밀번호가 변경되었습니다",
            type: "success",
          },
          { root: true }
        )
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          {
            text: "비밀번호 변경에 실패했습니다",
            type: "error",
          },
          { root: true }
        )
        throw new Error(error)
      }
    },
  },
  getters: {
    getEmail(state) {
      return state.signupFormData.email
    },
    getStep(state) {
      if (state.signupFormData.password) {
        return 2
      } else {
        return 1
      }
    },
    getSIgnupFormData(state) {
      return state.signupFormData
    },
    getIsAuthenticated(state) {
      return state.token.accessToken !== ""
    },
    getToken(state) {
      return state.token
    },
    getEmailAuthData(state) {
      return {
        requestData: { ...state.emailCertRequestDto },
        id: state.emailCertId,
      }
    },
    getFindPWFormData(state) {
      return state.findPasswordFormData
    },
  },
}

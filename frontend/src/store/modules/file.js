// import axios from "axios"
import FileAPI from "@/api/file"

export default {
  namespaced: true,
  state: {},
  mutations: {},
  actions: {
    async uploadFile({ commit }, file) {
      console.log(file)
      try {
        const uuid = await FileAPI.uploadFile(file)
        return uuid
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          {
            text: "파일 업로드에 실패했습니다",
            type: "error",
          },
          { root: true }
        )
      }
    },
    async uploadFile2({ commit }, file) {
      console.log(file)
      try {
        const resData = await FileAPI.uploadFile2(file)
        return resData
      } catch (error) {
        commit(
          "ADD_MESSAGE",
          {
            text: "파일 업로드에 실패했습니다",
            type: "error",
          },
          { root: true }
        )
      }
    },
  },
  getters: {},
}

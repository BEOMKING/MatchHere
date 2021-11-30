import ChatAPI from "@/api/chat"

export default {
  namespaced: true,
  state: { stompClient: null, targetUserId: null },
  mutations: {
    SET_CHAT_MATERIALS(state, { stompClient, targetUserId }) {
      state.stompClient = stompClient
      state.targetUserId = targetUserId
    },
  },
  actions: {
    async getChatList() {
      try {
        const resData = await ChatAPI.getChatList()
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async getChatRoomInfo(context, email) {
      try {
        const resData = await ChatAPI.getChatRoomInfo(email)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async getChatHistory(context, { roomId, pageNumber, size }) {
      try {
        const resData = await ChatAPI.getChatHistory(roomId, pageNumber, size)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
  },
  getters: {
    getStompClient(state) {
      return state.stompClient
    },
    getTargetUserId(state) {
      return state.targetUserId
    },
  },
}

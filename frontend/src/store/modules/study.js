import studyAPI from "@/api/study"

export default {
  namespaced: true,
  state: {},
  mutations: {},
  actions: {
    async createStudy({ commit }, data) {
      const res = await studyAPI.createStudy(data)
      commit(
        "ADD_MESSAGE",
        {
          text: res.text,
          type: res.type,
        },
        { root: true }
      )
      return res
    },
    async getStudyList({ commit }) {
      const res = await studyAPI.getStudyList()
      if (res.type === "error") {
        commit(
          "ADD_MESSAGE",
          {
            text: res.text,
            type: res.type,
          },
          { root: true }
        )
      }
      return res.data
    },
    async getStudy({ commit }, studyId) {
      await studyAPI.updateViewCountOfStudyArticle(studyId)
      const res = await studyAPI.getStudy(studyId)
      if (res.type === "error") {
        commit(
          "ADD_MESSAGE",
          {
            text: res.text,
            type: res.type,
          },
          { root: true }
        )
      }
      return res.data
    },
    async submitApplication(_, payload) {
      await studyAPI.submitApplication(payload)
    },
    async getStudyBoards(_, studyId) {
      return await studyAPI.getStudyBoards(studyId)
    },
    async createBoardArticle(_, data) {
      await studyAPI.createBoardArticle(data)
    },
    async getBoardArticles(_, boardId) {
      return await studyAPI.getBoardArticles(boardId)
    },
    async updateBoardArticle(_, payload) {
      return await studyAPI.updateBoardArticle(payload)
    },
    async getBoardArticle(_, articleId) {
      await studyAPI.updateViewCountOfBoardArticle(articleId)
      return await studyAPI.getBoardArticle(articleId)
    },
    async createArticleComment(_, payload) {
      return await studyAPI.createArticleComment(payload)
    },
    async getArticleComment(_, articleId) {
      return studyAPI.getArticleComment(articleId)
    },
    async deleteArticleComment(_, commentId) {
      await studyAPI.deleteArticleComment(commentId)
    },
  },
  getters: {},
}
